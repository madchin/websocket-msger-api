package com.example.service

import com.example.domain.model.Chat
import com.example.domain.dao.repository.ChatRepository
import com.example.domain.service.ChatService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatServiceImpl(private val chatRepository: ChatRepository) : ChatService {
    override suspend fun createChat(chat: Chat): Chat = withContext(Dispatchers.IO) {
        return@withContext chatRepository.createChat(chat).getOrThrow()
    }

    override suspend fun deleteChat(id: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext chatRepository.deleteChat(id).getOrThrow()
    }

    override suspend fun getChat(chatId: String, memberId: String): Chat = withContext(Dispatchers.IO) {
        return@withContext chatRepository.readChat(chatId,memberId).getOrThrow()
    }

    override suspend fun changeChatName(id: String, name: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext chatRepository.updateChatName(id, name).getOrThrow()
    }
    override suspend fun joinChat(chatId: String, memberUid: String): Chat = withContext(Dispatchers.IO) {
        return@withContext chatRepository.updateChatLastSeenMembers(chatId, memberUid).getOrThrow()
    }



}