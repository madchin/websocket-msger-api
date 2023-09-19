package com.example.repository

import com.example.model.Chat
import java.sql.Connection
import java.sql.Statement

class ChatRepositoryImpl(private val connection: Connection) : ChatRepository {
    companion object {
        private const val CREATE_TABLE_CHATS =
            "CREATE TABLE IF NOT EXISTS chats (" +
                    "id UUID DEFAULT uuid_generate_v4() PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    "members TEXT[], " +
                    "messages TEXT[]" +
                    ");"

        private const val INSERT_CHAT = "INSERT INTO chats (name, members) VALUES (?, ?);"

        private const val SELECT_CHAT_BY_ID = "SELECT id, name, members, messages FROM chats WHERE id = ?;"

        private const val UPDATE_CHAT_MEMBERS = "UPDATE chats SET members = ARRAY_APPEND(members, ?) WHERE id = ?;"

        private const val UPDATE_CHAT_MESSAGES = "UPDATE chats SET messages = ARRAY_APPEND(messages, ?) WHERE id = ?;"

        private const val UPDATE_CHAT_NAME = "UPDATE chats SET name = ? WHERE id = ?;"

        private const val DELETE_CHAT = "DELETE FROM chats WHERE id = ?;"

    }

    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_CHATS)
        statement.close()
    }

    override fun create(chat: Chat): String {
        val statement = connection.prepareStatement(INSERT_CHAT, Statement.RETURN_GENERATED_KEYS)
        val members = connection.createArrayOf("text", chat.members.toTypedArray())
        statement.setString(1, chat.name)
        statement.setArray(2, members)
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

    override fun read(id: String): Chat {
        val statement = connection.prepareStatement(SELECT_CHAT_BY_ID)
        statement.setString(1, id)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val name = resultSet.getString("name")
            val membersArray = resultSet.getArray("members").array as? Array<String>
            val members = membersArray?.toList() ?: emptyList()

            statement.close()
            return Chat(name = name, id = id, members = members)
        } else {
            statement.close()
            throw Exception("Record not found")
        }
    }

    override fun updateMembers(id: String, member: String) {
        val statement = connection.prepareStatement(UPDATE_CHAT_MEMBERS)
        statement.setString(1, member)
        statement.setString(2, id)
        statement.executeUpdate()
        statement.close()
    }

    override fun updateMessages(id: String, message: String) {
        val statement = connection.prepareStatement(UPDATE_CHAT_MESSAGES)
        statement.setString(1, message)
        statement.setString(2, id)
        statement.executeUpdate()
        statement.close()
    }

    override fun updateName(id: String, name: String) {
        val statement = connection.prepareStatement(UPDATE_CHAT_NAME)
        statement.setString(1, name)
        statement.setString(2, id)
        statement.executeUpdate()
        statement.close()
    }

    override fun delete(id: String) {
        val statement = connection.prepareStatement(DELETE_CHAT)
        statement.setString(1, id)
        statement.executeUpdate()
        statement.close()
    }

}