package com.example.service

import com.example.model.Chat
import com.example.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatServiceImpl(private val chatRepository: ChatRepository) : ChatService {
    override suspend fun createChat(chat: Chat): String = withContext(Dispatchers.IO) {
        chatRepository.create(chat)
    }

    override suspend fun deleteChat(id: String) = withContext(Dispatchers.IO) {
        chatRepository.delete(id)
    }

    override suspend fun getChat(id: String): Chat = withContext(Dispatchers.IO) {
        chatRepository.read(id)
    }

    override suspend fun addChatMember(id: String, member: String) = withContext(Dispatchers.IO) {
        chatRepository.updateMembers(id, member)
    }

    override suspend fun addChatMessage(id: String, message: String) = withContext(Dispatchers.IO) {
        chatRepository.updateMessages(id, message)
    }

    override suspend fun changeChatName(id: String, name: String) = withContext(Dispatchers.IO) {
        chatRepository.updateName(id, name)
    }
}