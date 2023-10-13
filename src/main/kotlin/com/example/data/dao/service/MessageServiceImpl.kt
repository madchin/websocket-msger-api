package com.example.data.dao.service

import com.example.domain.model.Message
import com.example.domain.dao.repository.MessageRepository
import com.example.domain.dao.service.MessageService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MessageServiceImpl(private val messageRepository: MessageRepository) : MessageService {
    override suspend fun readMessages(chatId: String): Result<List<Message>> = withContext(Dispatchers.IO) {
        return@withContext messageRepository.readMessages(chatId)
    }

    override suspend fun sendMessage(message: Message): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext messageRepository.createMessage(message)
    }
}