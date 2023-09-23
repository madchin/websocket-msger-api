package com.example.data.service

import com.example.data.model.Chat
import com.example.data.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatServiceImpl(private val chatRepository: ChatRepository) : ChatService {
    override suspend fun createChat(chat: Chat): Result<String> = withContext(Dispatchers.IO) {
        return@withContext chatRepository.createChat(chat)
    }

    override suspend fun deleteChat(id: String): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext chatRepository.deleteChat(id)
    }

    override suspend fun getChat(id: String): Result<Chat> = withContext(Dispatchers.IO) {
        return@withContext chatRepository.readChat(id)
    }

    override suspend fun changeChatName(id: String, name: String): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext chatRepository.updateChatName(id, name)
    }
}