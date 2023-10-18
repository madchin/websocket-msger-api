package com.example.service

import com.example.domain.model.Chat
import com.example.domain.dao.repository.ChatRepository
import com.example.domain.service.ChatService
import com.example.util.ForbiddenException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatServiceImpl(private val chatRepository: ChatRepository) : ChatService {
    override suspend fun createChat(chat: Chat, userId: String): Chat = withContext(Dispatchers.IO) {
        val chatWithOwner =
            chat.copy(lastSeenMembers = listOf(mapOf(userId to System.currentTimeMillis())))
        return@withContext chatRepository.createChat(chatWithOwner).getOrThrow()
    }

    override suspend fun deleteChat(chatId: String, userId: String): Boolean =
        withContext(Dispatchers.IO) {
            val chat = chatRepository.readChat(chatId).getOrThrow()
            val isChatMember = chat.lastSeenMembers.singleOrNull { it.keys.contains(userId) } != null
            if (!isChatMember) {
                throw ForbiddenException
            }
            return@withContext chatRepository.deleteChat(chatId).getOrThrow()
        }

    override suspend fun getChat(chatId: String, userId: String): Chat =
        withContext(Dispatchers.IO) {
            val chat = chatRepository.readChat(chatId).getOrThrow()
            val isChatMember = chat.lastSeenMembers.singleOrNull { it.keys.contains(userId) } != null
            if (!isChatMember) {
                throw ForbiddenException
            }
            return@withContext chat
        }

    override suspend fun changeChatName(chatId: String, name: String, userId: String): Boolean =
        withContext(Dispatchers.IO) {
            val chat = chatRepository.readChat(chatId).getOrThrow()
            val isChatMember = chat.lastSeenMembers.singleOrNull { it.keys.contains(userId) } != null
            if (!isChatMember) {
                throw ForbiddenException
            }
            return@withContext chatRepository.updateChatName(chatId, name).getOrThrow()
        }

    override suspend fun joinChat(chatId: String, userId: String): Chat =
        withContext(Dispatchers.IO) {
            chatRepository.readChat(chatId).getOrThrow().also {
                return@withContext chatRepository.updateChatLastSeenMembers(chatId, userId).getOrThrow()
            }
        }


}