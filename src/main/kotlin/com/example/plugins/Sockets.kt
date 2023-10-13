package com.example.plugins

import com.example.domain.dao.Services
import com.example.domain.model.Chat
import com.example.domain.model.ChatMember
import com.example.domain.model.Member
import com.example.domain.model.Message
import com.example.domain.socket.ChatSocketHandler
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import java.time.Duration

fun Application.configureSockets(services: Services, chatSocketHandler: ChatSocketHandler) {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }
    routing {
        webSocket("/chat/{id}") { // websocketSession
            try {
                val chatId = call.parameters.getOrFail("id")
                val member = call.receive<Member>()
                val memberSession = this

                val chat = chatSocketHandler.joinChat(chatId, ChatMember(memberSession, member)).run {
                    onFailure {
                        call.respond(HttpStatusCode.BadRequest, it.message.toString())
                    }
                }

                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val text = frame.readText()
                        val message = Message(
                            chatId = (chat as Chat).id!!,
                            sender = (member as Member).name,
                            content = text
                        )
                        chatSocketHandler.sendMessage(message)
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            }
        }
    }
}
