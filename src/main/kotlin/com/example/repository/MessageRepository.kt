package com.example.repository

import com.example.model.Message

interface MessageRepository {
    fun create(message: Message)
    fun read(chatId: String): List<Message>
}