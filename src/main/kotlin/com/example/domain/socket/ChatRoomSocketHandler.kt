package com.example.domain.socket

import com.example.domain.model.ChatMember
import com.example.domain.model.Message
import io.ktor.websocket.*

interface ChatRoomSocketHandler {
    val chatMembers: MutableSet<ChatMember>
    suspend fun onReceiveMessage(frame: Frame, saveMessage: suspend (Message) -> Result<Boolean>)
    suspend fun onJoin(addChatMember: (MutableSet<ChatMember>) -> Unit)
}