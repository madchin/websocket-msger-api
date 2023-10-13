package com.example.data.dao.service

import com.example.domain.model.Chat
import com.example.domain.dao.repository.ChatRepository
import com.example.domain.dao.service.ChatService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatServiceImpl(private val chatRepository: ChatRepository) : ChatService {
    override suspend fun createChat(chat: Chat): Result<Chat> = withContext(Dispatchers.IO) {
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
    override suspend fun joinChat(chatId: String, memberUid: String): Result<Chat> = withContext(Dispatchers.IO) {
        return@withContext chatRepository.updateChatLastSeenMembers(chatId, memberUid)
    }



}