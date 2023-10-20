package com.example.dao.repository

import com.example.model.Chat

interface ChatRepository {
    suspend fun createChat(chat: Chat): Result<Chat>
    suspend fun readChat(chatId: String): Result<Chat>
    suspend fun updateChatName(chatId: String, name: String): Result<Boolean>
    suspend fun deleteChat(chatId: String): Result<Boolean>
    suspend fun updateChatLastSeenMembers(chat: Chat, memberUid: String): Result<Chat>
}