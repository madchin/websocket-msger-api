package com.example.repository

import com.example.model.Member
import kotlinx.serialization.json.Json
import java.sql.Connection
import java.sql.Statement

class MemberRepositoryImpl(private val connection: Connection) : MemberRepository {

    companion object {
        private const val CREATE_TABLE_MEMBERS = "CREATE TABLE IF NOT EXISTS members (" +
                "uid UUID, " +
                "name TEXT, " +
                "lastSeen JSONB, " +
                "FOREIGN KEY (uid) REFERENCES users(uid)" +
                ");"
        private const val INSERT_MEMBER = "INSERT INTO members (uid) VALUES (uuid_generate_v4());"
        private const val SELECT_MEMBER_BY_ID = "SELECT uid, name, lastSeen FROM members WHERE uid = ?;"
        private const val UPDATE_MEMBER_NAME = "UPDATE members SET name = ? WHERE id = ?;"
        private const val UPDATE_MEMBER_LAST_SEEN =
            "UPDATE members SET lastSeen = jsonb_set(lastSeen, '{?}', to_jsonb(CURRENT_TIMESTAMP)::jsonb WHERE id = ?;"
        private const val DELETE_MEMBER = "DELETE FROM members WHERE uid = ?;"
    }

    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_MEMBERS)
        statement.close()
    }

    override fun create(): String {
        val statement = connection.prepareStatement(INSERT_MEMBER, Statement.RETURN_GENERATED_KEYS)
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

    override fun read(uid: String): Member {
        val statement = connection.prepareStatement(SELECT_MEMBER_BY_ID)
        statement.setString(1, uid)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val name = resultSet.getString("name")
            val lastSeen = resultSet.getString("lastSeen")
            val parsedLastSeen = Json.decodeFromString<Map<String, Long>>(lastSeen)

            statement.close()
            return Member(uid = uid, name = name, lastSeen = parsedLastSeen)
        } else {
            statement.close()
            throw Exception("Record not found")
        }
    }

    override fun updateName(uid: String, name: String) {
        val statement = connection.prepareStatement(UPDATE_MEMBER_NAME)
        statement.setString(1, name)
        statement.setString(2, uid)
        statement.executeUpdate()
        statement.close()
    }

    override fun updateLastSeen(uid: String, chatId: String) {
        val statement = connection.prepareStatement(UPDATE_MEMBER_LAST_SEEN)
        statement.setString(1, chatId)
        statement.setString(2, uid)
        statement.executeUpdate()
        statement.close()
    }

    override fun delete(uid: String) {
        val statement = connection.prepareStatement(DELETE_MEMBER)
        statement.setString(1, uid)
        statement.executeUpdate()
        statement.close()
    }

}