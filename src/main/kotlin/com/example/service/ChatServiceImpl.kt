package com.example.service

import com.example.domain.dao.repository.ChatRepository
import com.example.domain.model.Chat
import com.example.domain.model.ChatDTO
import com.example.domain.service.ChatService
import com.example.util.ExplicitException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatServiceImpl(private val chatRepository: ChatRepository) : ChatService {

    private fun ensureUserIsChatMember(chat: Chat, userId: String) {
        chat.lastSeenMembers.singleOrNull { it.keys.contains(userId) } ?: throw ExplicitException.Forbidden
    }
    override suspend fun createChat(chat: ChatDTO, userId: String): Chat = withContext(Dispatchers.IO) {
        val chatWithOwner =
            chat.copy(lastSeenMembers = listOf(mapOf(userId to System.currentTimeMillis())))
        return@withContext chatRepository.createChat(chatWithOwner.toChat()).getOrThrow()
    }

    override suspend fun deleteChat(chatId: String, userId: String): Boolean =
        withContext(Dispatchers.IO) {
            val chat = chatRepository.readChat(chatId).getOrThrow()
            ensureUserIsChatMember(chat, userId)

            return@withContext chatRepository.deleteChat(chatId).getOrThrow()
        }

    override suspend fun getChat(chatId: String, userId: String): Chat =
        withContext(Dispatchers.IO) {
            val chat = chatRepository.readChat(chatId).getOrThrow()
            ensureUserIsChatMember(chat, userId)

            return@withContext chat
        }

    override suspend fun changeChatName(chatId: String, name: String, userId: String): Boolean =
        withContext(Dispatchers.IO) {
            val chat = chatRepository.readChat(chatId).getOrThrow()
            ensureUserIsChatMember(chat, userId)

            return@withContext chatRepository.updateChatName(chatId, name).getOrThrow()
        }

    override suspend fun joinChat(chatId: String, userId: String): Chat =
        withContext(Dispatchers.IO) {
            val existingChat = chatRepository.readChat(chatId).getOrThrow()
            val chatMembersWithoutJoiningUser = existingChat.lastSeenMembers.filterNot { it.keys.contains(userId) }
            val chatWithoutJoiningUser = existingChat.copy(lastSeenMembers = chatMembersWithoutJoiningUser)

            return@withContext chatRepository.updateChatLastSeenMembers(chatWithoutJoiningUser, userId).getOrThrow()
        }


}