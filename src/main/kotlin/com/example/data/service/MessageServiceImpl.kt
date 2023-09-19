package com.example.data.service

import com.example.data.model.Message
import com.example.data.repository.MessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MessageServiceImpl(private val messageRepository: MessageRepository) : MessageService {
    override suspend fun readMessages(chatId: String): List<Message> = withContext(Dispatchers.IO) {
        messageRepository.readMessages(chatId)
    }

    override suspend fun sendMessage(message: Message) = withContext(Dispatchers.IO) {
        messageRepository.createMessage(message)
    }
}