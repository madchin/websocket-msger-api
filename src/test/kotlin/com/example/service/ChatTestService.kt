package com.example.service

import com.example.dao.repository.ChatRepository
import com.example.dao.repository.MessageRepository
import com.example.model.Chat
import com.example.model.ChatDTO
import com.example.model.Message

class ChatTestService(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository
) : ChatService {
    override suspend fun createChat(chat: ChatDTO, userId: String): Chat {
        TODO("Not yet implemented")
    }

    override suspend fun getChat(chatId: String, userId: String): Chat {
        TODO("Not yet implemented")
    }

    override suspend fun changeChatName(chatId: String, name: String, userId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteChat(chatId: String, userId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun joinChat(chatId: String, userId: String): Chat {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(message: Message): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun readMessages(chatId: String): List<Message> {
        TODO("Not yet implemented")
    }
}