package com.example.models

import java.sql.Connection

data class Member(val name: String, val lastSeen: Map<String, Long>)

class MemberService(private val connection: Connection) {

    companion object {
        private const val CREATE_TABLE_MEMBERS = "CREATE TABLE IF NOT EXISTS members (" +
                "id UUID DEFAULT uuid_generate_v4() PRIMARY KEY, " +
                "name TEXT, " +
                "lastSeen JSONB" +
                ");"

        private const val insert = "INSERT INTO members (id) VALUES (uuid_generate_v4());"
        private fun selectById(id: String) = "SELECT id, name, lastSeen FROM members WHERE id = $id;"

        private fun updateName(id: String, name: String) = "UPDATE members SET name = $name WHERE id = $id;"

        private fun updateLastSeen(id: String, chatId: String) = "UPDATE members SET lastSeen = jsonb_set(lastSeen, '{$chatId}', to_jsonb(CURRENT_TIMESTAMP)::jsonb WHERE id = $id;"

        private fun delete(id: String) = "DELETE FROM members WHERE id = $id;"
    }
    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_MEMBERS)
        statement.close()
    }

}