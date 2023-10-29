package com.example.socket

import com.example.model.ChatMember
import io.ktor.websocket.*

interface ChatRoomSocketHandler {
    val chatMembers: MutableSet<ChatMember>
    suspend fun onJoin(memberSession: DefaultWebSocketSession, userId: String, chatId: String)

    suspend fun broadcastMessage(chatId: String, frame: Frame)

    fun onLeave(memberSession: DefaultWebSocketSession, userId: String)
}