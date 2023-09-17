package com.example.models

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.Timestamp

data class Message(
    val chatId: String? = null,
    val sender: String,
    val content: String,
    val timestamp: Timestamp? = null
)

class MessageService(private val connection: Connection) {
    companion object {
        private const val CREATE_TABLE_MESSAGES = "CREATE TABLE IF NOT EXISTS messages (" +
                "id SERIAL PRIMARY KEY, " +
                "chatId UUID, " +
                "sender TEXT, " +
                "content TEXT, " +
                "timestamp TIMESTAMPZ DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (chatId) REFERENCES chat(chatId)" +
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

    suspend fun create(message: Message) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_MESSAGE)
        statement.setString(1, message.chatId)
        statement.setString(2, message.sender)
        statement.setString(3, message.content)
        statement.executeUpdate()
        statement.close()
    }

    suspend fun read(chatId: String): List<Message> = withContext(Dispatchers.IO) {
        val messages = mutableListOf<Message>()
        val statement = connection.prepareStatement(SELECT_MESSAGES_BY_CHAT_ID)
        statement.setString(1, chatId)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val sender = resultSet.getString("sender")
            val content = resultSet.getString("content")
            val timestamp = resultSet.getTimestamp("timestamp")
            messages.add(Message(sender = sender, content = content, timestamp = timestamp))
        }
        statement.close()
        if (messages.isEmpty()) {
            throw Exception("Record not found")
        }
        return@withContext messages
    }
}