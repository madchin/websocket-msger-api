package com.example.repository

import com.example.model.Message

interface MessageRepository {
    suspend fun create(message: Message)
    suspend fun read(chatId: String): List<Message>
}