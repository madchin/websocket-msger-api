package com.example.models

import kotlinx.serialization.Serializable
import java.sql.Connection

@Serializable
data class Chat(val id: String, val name: String, val members: List<String>, val messages: List<String>)

class ChatService(private val connection: Connection) {
    companion object {
        private const val CREATE_TABLE_CHATS =
            "CREATE TABLE IF NOT EXISTS chats (" +
                    "id UUID DEFAULT uuid_generate_v4() PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    "members TEXT[], " +
                    "messages TEXT[]" +
                    ");"

        private fun insert(chat: Chat): String =
            "INSERT INTO chats (name, members) VALUES (${chat.name}, ${chat.members}) RETURNING id;"

        private fun selectById(id: String) = "SELECT id, name, members, messages FROM chats WHERE id = $id;"
        private fun updateMembers(id: String, member: String) =
            "UPDATE chats SET members = ARRAY_APPEND(members, $member) WHERE id = $id;"

        private fun updateMessages(id: String, message: String) =
            "UPDATE chats SET messages = ARRAY_APPEND(messages, $message) WHERE id = $id;"

        private fun updateName(id: String, name: String) = "UPDATE chats SET name = $name WHERE id = $id;"
        private fun delete(id: String) = "DELETE FROM chats WHERE id = $id;"

    }

    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_CHATS)
        statement.close()
    }
}