package com.example.data.repository

import com.example.data.model.Chat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.Statement

class ChatRepositoryImpl(private val connection: Connection) : ChatRepository {
    companion object {
        private const val CREATE_TABLE_CHATS =
            "CREATE TABLE IF NOT EXISTS chats (" +
                    "id UUID DEFAULT uuid_generate_v4() PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    ");"

        private const val INSERT_CHAT = "INSERT INTO chats (name) VALUES (?);"

        private const val SELECT_CHAT_BY_ID = "SELECT id, name FROM chats WHERE id = ?;"

        private const val UPDATE_CHAT_NAME = "UPDATE chats SET name = ? WHERE id = ?;"

        private const val DELETE_CHAT = "DELETE FROM chats WHERE id = ?;"

    }

    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_CHATS)
        statement.close()
    }

    override suspend fun createChat(chat: Chat): String {
        val statement = connection.prepareStatement(INSERT_CHAT, Statement.RETURN_GENERATED_KEYS)
        statement.setString(1, chat.name)
        statement.executeUpdate()

        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            statement.close()
            return generatedKeys.getString(1)
        } else {
            statement.close()
            throw Exception("Unable to retrieve the id of the newly inserted chat")
        }
    }

    override suspend fun readChat(id: String): Chat = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_CHAT_BY_ID)
        statement.setString(1, id)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val name = resultSet.getString("name")

            statement.close()
            return@withContext Chat(name = name, id = id)
        } else {
            statement.close()
            throw Exception("Record not found")
        }
    }

    override suspend fun updateChatName(id: String, name: String) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_CHAT_NAME)
        statement.setString(1, name)
        statement.setString(2, id)
        statement.executeUpdate()
        statement.close()
    }

    override suspend fun deleteChat(id: String) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_CHAT)
        statement.setString(1, id)
        statement.executeUpdate()
        statement.close()
    }

}