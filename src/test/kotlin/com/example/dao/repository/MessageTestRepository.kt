package com.example.dao.repository

import com.example.model.Message

class MessageTestRepository : MessageRepository {
    override suspend fun createMessage(message: Message): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun readMessages(chatId: String): Result<List<Message>> {
        TODO("Not yet implemented")
    }
}