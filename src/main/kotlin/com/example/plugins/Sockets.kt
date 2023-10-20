package com.example.plugins

import com.example.model.ChatMember
import com.example.socket.ChatMemberSocketHandler
import com.example.socket.ChatRoomSocketHandler
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
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
            chatMemberSocketHandler.joinChat(chatId, memberId).let { currentMember ->
                val chatMember = ChatMember(session = memberSession, member = currentMember)
                chatRoomSocketHandler.onJoin {
                    it.add(chatMember)
                }
            }
            chatRoomSocketHandler.onReceiveMessage(memberSession, chatRoomSocketHandler::broadcastMessage)
        }
    }
}
