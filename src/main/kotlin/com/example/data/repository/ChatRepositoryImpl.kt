package com.example.data.repository

import com.example.data.model.Chat
import com.example.data.util.GenericException
import io.ktor.server.plugins.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.Statement

class ChatRepositoryImpl(private val connection: Connection) : ChatRepository {
    companion object {
        private const val CREATE_TABLE_CHATS =
            "CREATE TABLE IF NOT EXISTS chats (" +
                    "id UUID DEFAULT uuid_generate_v4() PRIMARY KEY, " +
                    "name VARCHAR(255)" +
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

    override suspend fun createChat(chat: Chat): Result<String> = withContext(Dispatchers.IO) {
        try {
            val statement = connection.prepareStatement(INSERT_CHAT, Statement.RETURN_GENERATED_KEYS)
            statement.setString(1, chat.name)
            statement.executeUpdate()
            val generatedKeys = statement.generatedKeys
            statement.close()

            if (generatedKeys.next()) {
                return@withContext Result.success(generatedKeys.getString(1))
            }
            return@withContext Result.failure(Exception("Unable to retrieve the id of the newly inserted chat"))
        } catch (e: Throwable) {
            return@withContext Result.failure(GenericException())
        }
    }

    override suspend fun readChat(id: String): Result<Chat> = withContext(Dispatchers.IO) {
        try {
            val statement = connection.prepareStatement(SELECT_CHAT_BY_ID)
            statement.setString(1, id)
            val resultSet = statement.executeQuery()
            statement.close()

            if (resultSet.next()) {
                val name = resultSet.getString("name")
                return@withContext Result.success(Chat(name = name, id = id))
            }
            return@withContext Result.failure(NotFoundException("Chat with $id id not found"))
        } catch (e: Throwable) {
            return@withContext Result.failure(GenericException())
        }
    }

    override suspend fun updateChatName(id: String, name: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val statement = connection.prepareStatement(UPDATE_CHAT_NAME)
            statement.setString(1, name)
            statement.setString(2, id)
            statement.executeUpdate()
            statement.close()
            return@withContext Result.success(true)
        } catch (e: Throwable) {
            return@withContext Result.failure(GenericException())
        }
    }

    override suspend fun deleteChat(id: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val statement = connection.prepareStatement(DELETE_CHAT)
            statement.setString(1, id)
            statement.executeUpdate()
            statement.close()
            return@withContext Result.success(true)
        } catch (e: Throwable) {
            return@withContext Result.failure(GenericException())
        }
    }

}