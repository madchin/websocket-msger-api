package com.example.data.repository

import com.example.data.model.Chat

interface ChatRepository {
    suspend fun createChat(chat: Chat): String
    suspend fun readChat(id: String): Chat
    suspend fun updateChatName(id: String, name: String)
    suspend fun deleteChat(id: String)
}