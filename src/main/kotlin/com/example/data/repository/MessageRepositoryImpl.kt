package com.example.data.repository

import com.example.data.model.Message
import io.ktor.server.plugins.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection

class MessageRepositoryImpl(private val connection: Connection) : MessageRepository {
    companion object {
        private const val CREATE_TABLE_MESSAGES = "CREATE TABLE IF NOT EXISTS messages (" +
                "id SERIAL PRIMARY KEY, " +
                "chatId UUID, " +
                "sender TEXT, " +
                "content TEXT, " +
                "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (chatId) REFERENCES chats(id)" +
                ");"
        private const val INSERT_MESSAGE = "INSERT INTO messages (chatId, sender, content) VALUES (?, ?, ?);"
        private const val SELECT_MESSAGES_BY_CHAT_ID =
            "SELECT (sender,content, timestamp) FROM messages WHERE chatId = ?;"
    }

    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_MESSAGES)
        statement.close()
    }

    override suspend fun createMessage(message: Message): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val statement = connection.prepareStatement(INSERT_MESSAGE)
            statement.setString(1, message.chatId)
            statement.setString(2, message.sender)
            statement.setString(3, message.content)
            statement.executeUpdate()
            statement.close()
            return@withContext Result.success(true)

        } catch (e: Throwable) {
            return@withContext Result.failure(e)
        }
    }

    override suspend fun readMessages(chatId: String): Result<List<Message>> = withContext(Dispatchers.IO) {
        val messages = mutableListOf<Message>()
        try {
            val statement = connection.prepareStatement(SELECT_MESSAGES_BY_CHAT_ID)
            statement.setString(1, chatId)
            val resultSet = statement.executeQuery()
            statement.close()

            while (resultSet.next()) {
                val sender = resultSet.getString("sender")
                val content = resultSet.getString("content")
                val timestamp = resultSet.getTimestamp("timestamp")?.toString()?.toLong()

                messages.add(Message(sender = sender, content = content, timestamp = timestamp))
            }
            if (messages.isEmpty()) throw NotFoundException("Messages in chat with $chatId id not found")
            return@withContext Result.success(messages)
        } catch (e: Throwable) {
            return@withContext Result.failure(e)
        }
    }
}