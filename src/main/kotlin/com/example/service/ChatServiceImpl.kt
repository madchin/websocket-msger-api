package com.example.service

import com.example.dao.repository.ChatRepository
import com.example.dao.repository.MessageRepository
import com.example.model.Chat
import com.example.model.ChatDTO
import com.example.model.Message
import com.example.util.ExplicitException

private fun ensureUserIsChatMember(chat: Chat, userId: String) {
    chat.lastSeenMembers.singleOrNull { it.keys.contains(userId) } ?: throw ExplicitException.Forbidden
}

class ChatServiceImpl(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository
) : ChatService {

    override suspend fun createChat(chat: ChatDTO, userId: String, timestamp: Long): Chat {
        val chatWithOwner =
            chat.copy(lastSeenMembers = listOf(mapOf(userId to timestamp)))
        return chatRepository.createChat(chatWithOwner.toChat()).getOrThrow()
    }

    override suspend fun deleteChat(chatId: String, userId: String): Boolean =
        getChat(chatId, userId).let {
            chatRepository.deleteChat(chatId).getOrThrow()
        }


    override suspend fun getChat(chatId: String, userId: String): Chat =
        chatRepository.readChat(chatId).getOrThrow().apply {
            ensureUserIsChatMember(this, userId)
        }

    override suspend fun findChat(chatId: String): Chat =
        chatRepository.readChat(chatId = chatId).getOrThrow()

    override suspend fun changeChatName(chatId: String, name: String, userId: String): Chat =
        getChat(chatId, userId).let {
            chatRepository.updateChatName(chatId, name).getOrThrow()
        }

    override suspend fun joinChat(chatId: String, userId: String, timestamp: Long): Chat =
        findChat(chatId).let { chat ->
            val chatMembersWithoutJoiningUser = chat.lastSeenMembers.filterNot { it.keys.contains(userId) }
            val chatWithoutJoiningUser = chat.copy(lastSeenMembers = chatMembersWithoutJoiningUser)

            chatRepository.updateChatLastSeenMembers(chatWithoutJoiningUser, userId, timestamp).getOrThrow()
        }

    override suspend fun sendMessage(message: Message): Boolean =
        getChat(message.chatId, message.sender).let {
            messageRepository.createMessage(message).getOrThrow()
        }

    override suspend fun readMessages(chatId: String, userId: String): List<Message> =
        getChat(chatId, userId).let {
            messageRepository.readMessages(chatId).getOrThrow()
        }
}