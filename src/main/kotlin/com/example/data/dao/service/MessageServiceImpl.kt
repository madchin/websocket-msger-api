package com.example.data.dao.service

import com.example.domain.model.Message
import com.example.domain.dao.repository.MessageRepository
import com.example.domain.dao.service.MessageService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MessageServiceImpl(private val messageRepository: MessageRepository) : MessageService {
    override suspend fun readMessages(chatId: String): List<Message> = withContext(Dispatchers.IO) {
        return@withContext messageRepository.readMessages(chatId).getOrThrow()
    }

    override suspend fun sendMessage(message: Message): Boolean = withContext(Dispatchers.IO) {
        return@withContext messageRepository.createMessage(message).getOrThrow()
    }
}