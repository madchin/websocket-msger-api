package com.example.domain.dao.repository

import com.example.domain.model.Message

interface MessageRepository {
    suspend fun createMessage(message: Message): Result<Boolean>
    suspend fun readMessages(chatId: String): Result<List<Message>>
}