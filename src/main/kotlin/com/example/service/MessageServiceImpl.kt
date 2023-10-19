package com.example.service

import com.example.domain.dao.repository.MessageRepository
import com.example.domain.model.Message
import com.example.domain.service.MessageService

class MessageServiceImpl(private val messageRepository: MessageRepository) : MessageService {
    override suspend fun readMessages(chatId: String): List<Message> {
        return messageRepository.readMessages(chatId).getOrThrow()
    }

    override suspend fun saveMessage(message: Message): Boolean {
        return messageRepository.createMessage(message).getOrThrow()
    }
}