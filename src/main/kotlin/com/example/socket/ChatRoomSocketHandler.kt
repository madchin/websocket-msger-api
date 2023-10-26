package com.example.socket

import com.example.model.ChatMember
import io.ktor.websocket.*

interface ChatRoomSocketHandler {
    val chatMembers: MutableSet<ChatMember>

    fun onJoin(chatMember: ChatMember)

    suspend fun broadcastMessage(chatId: String, frame: Frame)

    fun onLeave(chatMember: ChatMember)
}