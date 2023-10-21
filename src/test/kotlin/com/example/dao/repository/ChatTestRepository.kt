package com.example.dao.repository

import com.example.model.Chat

class ChatTestRepository : ChatRepository {
    override suspend fun createChat(chat: Chat): Result<Chat> {
        TODO("Not yet implemented")
    }

    override suspend fun readChat(chatId: String): Result<Chat> {
        TODO("Not yet implemented")
    }

    override suspend fun updateChatName(chatId: String, name: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteChat(chatId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateChatLastSeenMembers(chat: Chat, memberUid: String): Result<Chat> {
        TODO("Not yet implemented")
    }
}