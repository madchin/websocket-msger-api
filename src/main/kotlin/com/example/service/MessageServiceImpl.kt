package com.example.service

import com.example.model.Message
import com.example.repository.MessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MessageServiceImpl(private val messageRepository: MessageRepository) : MessageService {
    override suspend fun readMessages(chatId: String): List<Message> = withContext(Dispatchers.IO) {
        messageRepository.read(chatId)
    }

    override suspend fun sendMessage(message: Message) = withContext(Dispatchers.IO) {
        messageRepository.create(message)
    }
}