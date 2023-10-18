package com.example.domain.dao.repository

import com.example.domain.model.Chat

interface ChatRepository {
    suspend fun createChat(chat: Chat): Result<Chat>
    suspend fun readChat(chatId: String): Result<Chat>
    suspend fun updateChatName(chatId: String, name: String): Result<Boolean>
    suspend fun deleteChat(chatId: String): Result<Boolean>
    suspend fun updateChatLastSeenMembers(chatId: String, memberUid: String): Result<Chat>
}