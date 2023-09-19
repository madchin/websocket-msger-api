package com.example.repository

import com.example.model.Chat

interface ChatRepository {
    suspend fun create(chat: Chat): String
    suspend fun read(id: String): Chat
    suspend fun updateName(id: String, name: String)
    suspend fun delete(id: String)
}