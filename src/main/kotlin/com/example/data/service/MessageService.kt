package com.example.data.service

import com.example.data.model.Message

interface MessageService {
    suspend fun sendMessage(message: Message)
    suspend fun readMessages(chatId: String): List<Message>
}