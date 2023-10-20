package com.example.socket

import com.example.model.ChatMember
import com.example.model.Member
import io.ktor.websocket.*
import kotlin.reflect.KSuspendFunction1

interface ChatMemberSocketHandler {
    suspend fun joinChat(chatId: String, memberId: String, memberSession: DefaultWebSocketSession, onJoin: suspend (ChatMember) -> Unit)

}