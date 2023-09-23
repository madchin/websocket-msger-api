package com.example.data.service

import com.example.data.model.Message

interface MessageService {
    suspend fun sendMessage(message: Message): Result<Boolean>
    suspend fun readMessages(chatId: String): Result<List<Message>>
}