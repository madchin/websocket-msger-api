package com.example.data.repository

import com.example.data.dao.model.Message

interface MessageRepository {
    suspend fun createMessage(message: Message): Result<Boolean>
    suspend fun readMessages(chatId: String): Result<List<Message>>
}