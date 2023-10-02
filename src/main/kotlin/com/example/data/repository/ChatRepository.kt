package com.example.data.repository

import com.example.data.dao.model.Chat

interface ChatRepository {
    suspend fun createChat(chat: Chat): Result<Chat>
    suspend fun readChat(id: String): Result<Chat>
    suspend fun updateChatName(id: String, name: String): Result<Boolean>
    suspend fun deleteChat(id: String): Result<Boolean>
}