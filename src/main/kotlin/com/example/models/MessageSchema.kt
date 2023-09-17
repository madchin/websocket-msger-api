package com.example.models

import java.sql.Connection
import java.sql.Timestamp

data class Message(val chatId: String, val sender: String, val content: String)

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

        private fun insert(message: Message) =
            "INSERT INTO messages (chatId, sender, content) VALUES (" +
                    "${message.chatId}, " +
                    "${message.sender}, " +
                    "${message.content} " +
                    "RETURNING id, content, timestamp;"

        private fun selectByChatId(chatId: String) = "SELECT (sender,content, timestamp) FROM messages WHERE chatId = $chatId;"
    }

    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_MESSAGES)
        statement.close()
    }
}