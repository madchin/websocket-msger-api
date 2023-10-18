package com.example.domain.socket

import com.example.domain.model.ChatMember
import io.ktor.websocket.*
import kotlin.reflect.KSuspendFunction1

interface ChatRoomSocketHandler {
    val chatMembers: MutableSet<ChatMember>
    suspend fun broadcastMessage(frame: Frame)
    suspend fun onReceiveMessage(session: DefaultWebSocketSession, broadcastMessage: suspend (Frame) -> Unit)
    suspend fun onJoin(addChatMember: (MutableSet<ChatMember>) -> Unit)
}