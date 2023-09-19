package com.example.service

import com.example.model.Chat
import com.example.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatServiceImpl(private val chatRepository: ChatRepository) : ChatService {
    override suspend fun createChat(chat: Chat): String = withContext(Dispatchers.IO) {
        chatRepository.createChat(chat)
    }

    override suspend fun deleteChat(id: String) = withContext(Dispatchers.IO) {
        chatRepository.deleteChat(id)
    }

    override suspend fun getChat(id: String): Chat = withContext(Dispatchers.IO) {
        chatRepository.readChat(id)
    }

    override suspend fun changeChatName(id: String, name: String) = withContext(Dispatchers.IO) {
        chatRepository.updateChatName(id, name)
    }
}