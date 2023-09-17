package com.example.models

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.sql.Connection
import java.sql.Statement

data class Member(val uid: String, val name: String, val lastSeen: Map<String, Long>)

class MemberService(private val connection: Connection) {

    companion object {
        private const val CREATE_TABLE_MEMBERS = "CREATE TABLE IF NOT EXISTS members (" +
                "id UUID DEFAULT uuid_generate_v4() PRIMARY KEY, " +
                "name TEXT, " +
                "lastSeen JSONB" +
                ");"
        private const val INSERT_MEMBER = "INSERT INTO members (id) VALUES (uuid_generate_v4());"
        private const val SELECT_MEMBER_BY_ID = "SELECT id, name, lastSeen FROM members WHERE id = ?;"
        private const val UPDATE_MEMBER_NAME = "UPDATE members SET name = ? WHERE id = ?;"
        private const val UPDATE_MEMBER_LAST_SEEN =
            "UPDATE members SET lastSeen = jsonb_set(lastSeen, '{?}', to_jsonb(CURRENT_TIMESTAMP)::jsonb WHERE id = ?;"
        private const val DELETE_MEMBER = "DELETE FROM members WHERE id = ?;"
    }

    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_MEMBERS)
        statement.close()
    }

    suspend fun create(): String = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_MEMBER, Statement.RETURN_GENERATED_KEYS)
        statement.executeUpdate()

        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            statement.close()
            return@withContext generatedKeys.getString(1)
        } else {
            statement.close()
            throw Exception("Unable to retrieve the id of the newly inserted chat")
        }
    }

    suspend fun read(uid: String): Member = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_MEMBER_BY_ID)
        statement.setString(1, uid)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val name = resultSet.getString("name")
            val lastSeen = resultSet.getString("lastSeen")
            val parsedLastSeen = Json.decodeFromString<Map<String, Long>>(lastSeen)

            statement.close()
            return@withContext Member(uid = uid, name = name, lastSeen = parsedLastSeen)
        } else {
            statement.close()
            throw Exception("Record not found")
        }
    }

    suspend fun updateName(uid: String, name: String) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_MEMBER_NAME)
        statement.setString(1, name)
        statement.setString(2, uid)
        statement.executeUpdate()
        statement.close()
    }

    suspend fun updateLastSeen(uid: String, chatId: String) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_MEMBER_LAST_SEEN)
        statement.setString(1, chatId)
        statement.setString(2, uid)
        statement.executeUpdate()
        statement.close()
    }

    suspend fun delete(uid: String) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_MEMBER)
        statement.setString(1, uid)
        statement.executeUpdate()
        statement.close()
    }

}