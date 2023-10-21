package com.example.socket

import com.example.model.ChatMember
import io.ktor.websocket.*

interface ChatMemberSocketHandler {
    suspend fun joinChat(chatId: String, memberId: String, memberSession: DefaultWebSocketSession, onJoin: suspend (ChatMember) -> Unit)

}