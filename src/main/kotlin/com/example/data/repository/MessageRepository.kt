package com.example.data.repository

import com.example.data.model.Message

interface MessageRepository {
    suspend fun createMessage(message: Message)
    suspend fun readMessages(chatId: String): List<Message>
}