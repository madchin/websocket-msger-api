package com.example.domain.dao.service

import com.example.domain.model.Chat

interface ChatService {
    suspend fun createChat(chat: Chat): Result<Chat>
    suspend fun getChat(id: String): Result<Chat>
    suspend fun changeChatName(id: String, name: String): Result<Boolean>
    suspend fun deleteChat(id: String): Result<Boolean>
    suspend fun joinChat(chatId: String, memberUid: String): Result<Chat>
}