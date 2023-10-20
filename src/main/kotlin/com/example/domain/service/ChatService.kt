package com.example.domain.service

import com.example.domain.model.Chat
import com.example.domain.model.ChatDTO
import com.example.domain.model.Message

interface ChatService  {
    suspend fun createChat(chat: ChatDTO, userId: String): Chat
    suspend fun getChat(chatId: String, userId: String): Chat
    suspend fun changeChatName(chatId: String, name: String, userId: String): Boolean
    suspend fun deleteChat(chatId: String, userId: String): Boolean
    suspend fun joinChat(chatId: String, userId: String): Chat
    suspend fun sendMessage(message: Message): Boolean
    suspend fun readMessages(chatId: String): List<Message>
}