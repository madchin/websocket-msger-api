package com.example.service

import com.example.model.Chat
import com.example.model.ChatDTO
import com.example.model.Message

interface ChatService {
    suspend fun createChat(chat: ChatDTO, userId: String, timestamp: Long = System.currentTimeMillis()): Chat
    suspend fun getChat(chatId: String, userId: String): Chat
    suspend fun findChat(chatId: String): Chat
    suspend fun changeChatName(chatId: String, name: String, userId: String): Chat
    suspend fun deleteChat(chatId: String, userId: String): Boolean
    suspend fun joinChat(chatId: String, userId: String, timestamp: Long = System.currentTimeMillis()): Chat
    suspend fun sendMessage(message: Message): Boolean
    suspend fun readMessages(chatId: String, userId: String): List<Message>
}