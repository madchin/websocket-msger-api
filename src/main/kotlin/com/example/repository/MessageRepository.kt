package com.example.repository

import com.example.model.Message

interface MessageRepository {
    suspend fun createMessage(message: Message)
    suspend fun readMessages(chatId: String): List<Message>
}