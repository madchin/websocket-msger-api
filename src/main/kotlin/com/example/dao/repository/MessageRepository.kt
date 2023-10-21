package com.example.dao.repository

import com.example.model.Message

interface MessageRepository {
    suspend fun createMessage(message: Message): Result<Boolean>
    suspend fun readMessages(chatId: String): Result<List<Message>>
}