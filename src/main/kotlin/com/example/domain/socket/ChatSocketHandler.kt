package com.example.domain.socket

import com.example.domain.model.Chat
import com.example.domain.model.ChatMember
import com.example.domain.model.Message

interface ChatSocketHandler {
    val chatMembers: MutableSet<ChatMember>
    suspend fun sendMessage(message: Message): Result<Boolean>
    suspend fun joinChat(chatId: String, chatMember: ChatMember): Result<Chat>
}