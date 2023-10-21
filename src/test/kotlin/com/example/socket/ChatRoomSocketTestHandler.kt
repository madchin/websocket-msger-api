package com.example.socket

import com.example.model.ChatMember
import com.example.service.ChatService
import io.ktor.websocket.*

class ChatRoomSocketTestHandler(
    private val chatService: ChatService
) : ChatRoomSocketHandler {
    override val chatMembers: MutableSet<ChatMember>
        get() = TODO("Not yet implemented")

    override suspend fun broadcastMessage(frame: Frame) {
        TODO("Not yet implemented")
    }

    override suspend fun onReceiveMessage(session: DefaultWebSocketSession, broadcastMessage: suspend (Frame) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun onJoin(chatMember: ChatMember) {
        TODO("Not yet implemented")
    }
}