package com.example.plugins

import com.example.domain.model.ChatMember
import com.example.domain.socket.ChatMemberSocketHandler
import com.example.domain.socket.ChatRoomSocketHandler
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import io.ktor.server.websocket.*
import kotlinx.serialization.json.Json
import java.time.Duration

fun Application.configureSockets(
    chatRoomSocketHandler: ChatRoomSocketHandler,
    chatMemberSocketHandler: ChatMemberSocketHandler
) {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }

    routing {
        webSocket("/chat/{id}") { // websocketSession
            val chatId = call.parameters.getOrFail("id")
            val memberId = call.request.queryParameters.getOrFail("member-id")
            val memberSession = this
            chatMemberSocketHandler.joinChat(chatId, memberId).run {
                val chatMember = ChatMember(session = memberSession, member = this.first)
                chatRoomSocketHandler.onJoin {
                    it.add(chatMember)
                }
            }
            for (frame in incoming) {
                chatRoomSocketHandler.onReceiveMessage(frame, chatMemberSocketHandler::sendMessage)
            }
        }
    }
}
