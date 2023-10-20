package com.example.service

import com.example.dao.repository.ChatRepository
import com.example.dao.repository.MessageRepository
import com.example.model.Chat
import com.example.model.ChatDTO
import com.example.model.Message
import com.example.socket.ChatMemberSocketHandler
import com.example.socket.ChatRoomSocketHandler
import com.example.util.ExplicitException

class ChatServiceImpl(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository
) : ChatService {

    private fun ensureUserIsChatMember(chat: Chat, userId: String) {
        chat.lastSeenMembers.singleOrNull { it.keys.contains(userId) } ?: throw ExplicitException.Forbidden
    }

    override suspend fun createChat(chat: ChatDTO, userId: String): Chat {
        val chatWithOwner =
            chat.copy(lastSeenMembers = listOf(mapOf(userId to System.currentTimeMillis())))
        return chatRepository.createChat(chatWithOwner.toChat()).getOrThrow()
    }

    override suspend fun deleteChat(chatId: String, userId: String): Boolean =
        chatRepository.readChat(chatId).getOrThrow().let { chat ->
            ensureUserIsChatMember(chat, userId)

            chatRepository.deleteChat(chatId).getOrThrow()
        }


    override suspend fun getChat(chatId: String, userId: String): Chat =
        chatRepository.readChat(chatId).getOrThrow().apply {
            ensureUserIsChatMember(this, userId)
        }

    override suspend fun changeChatName(chatId: String, name: String, userId: String): Boolean =
        chatRepository.readChat(chatId).getOrThrow().let {
            ensureUserIsChatMember(it, userId)
            chatRepository.updateChatName(chatId, name).getOrThrow()
        }

    override suspend fun joinChat(chatId: String, userId: String): Chat =
        chatRepository.readChat(chatId).getOrThrow().let { chat ->
            val chatMembersWithoutJoiningUser = chat.lastSeenMembers.filterNot { it.keys.contains(userId) }
            val chatWithoutJoiningUser = chat.copy(lastSeenMembers = chatMembersWithoutJoiningUser)

            chatRepository.updateChatLastSeenMembers(chatWithoutJoiningUser, userId).getOrThrow()
        }

    override suspend fun sendMessage(message: Message): Boolean =
        messageRepository.createMessage(message).getOrThrow()

    override suspend fun readMessages(chatId: String): List<Message> =
        messageRepository.readMessages(chatId).getOrThrow()
}