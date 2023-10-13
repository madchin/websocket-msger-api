package com.example.domain.dao.service

import com.example.domain.model.Message

interface MessageService {
    suspend fun sendMessage(message: Message): Result<Boolean>
    suspend fun readMessages(chatId: String): Result<List<Message>>
}