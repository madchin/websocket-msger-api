package com.example.domain.dao.service

import com.example.domain.model.Message

interface MessageService {
    suspend fun saveMessage(message: Message): Boolean
    suspend fun readMessages(chatId: String): List<Message>
}