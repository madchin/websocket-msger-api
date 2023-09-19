package com.example.service

import com.example.model.Message

interface MessageService {
    suspend fun sendMessage(message: Message)
    suspend fun readMessages(chatId: String): List<Message>
}