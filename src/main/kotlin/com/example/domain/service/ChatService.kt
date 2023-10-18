package com.example.domain.service

import com.example.domain.model.Chat

interface ChatService {
    suspend fun createChat(chat: Chat, userId: String): Chat
    suspend fun getChat(chatId: String, userId: String): Chat
    suspend fun changeChatName(chatId: String, name: String, userId: String): Boolean
    suspend fun deleteChat(chatId: String, userId: String): Boolean
    suspend fun joinChat(chatId: String, userId: String): Chat
}