package com.example.data.repository

import com.example.data.model.Member
import com.example.data.util.GenericException
import io.ktor.server.plugins.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    override suspend fun createMember(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val statement = connection.prepareStatement(INSERT_MEMBER, Statement.RETURN_GENERATED_KEYS)
            statement.executeUpdate()
            val generatedKeys = statement.generatedKeys
            statement.close()

            if (generatedKeys.next()) {
                return@withContext Result.success(generatedKeys.getString(1))
            }
            return@withContext Result.failure(Exception("Unable to retrieve the id of the newly inserted member"))

        } catch (e: Throwable) {
            return@withContext Result.failure(GenericException)
        }

    }

    override suspend fun readMember(uid: String): Result<Member> = withContext(Dispatchers.IO) {
        try {
            val statement = connection.prepareStatement(SELECT_MEMBER_BY_ID)
            statement.setString(1, uid)
            val resultSet = statement.executeQuery()
            statement.close()

            if (resultSet.next()) {
                val name = resultSet.getString("name")
                val lastSeen = resultSet.getString("lastSeen")
                val parsedLastSeen = Json.decodeFromString<Map<String, Long>>(lastSeen)

                return@withContext Result.success(Member(uid = uid, name = name, lastSeen = parsedLastSeen))
            }
            return@withContext Result.failure(NotFoundException("Member with $uid uid not found"))
        } catch (e: Throwable) {
            return@withContext Result.failure(GenericException)
        }
    }

    override suspend fun updateMemberName(uid: String, name: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val statement = connection.prepareStatement(UPDATE_MEMBER_NAME)
            statement.setString(1, name)
            statement.setString(2, uid)
            statement.executeUpdate()
            statement.close()
            return@withContext Result.success(true)
        } catch (e: Throwable) {
            return@withContext Result.failure(GenericException)
        }
    }

    override suspend fun updateMemberLastSeen(uid: String, chatId: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val statement = connection.prepareStatement(UPDATE_MEMBER_LAST_SEEN)
                statement.setString(1, chatId)
                statement.setString(2, uid)
                statement.executeUpdate()
                statement.close()
                return@withContext Result.success(true)
            } catch (e: Throwable) {
                return@withContext Result.failure(GenericException)
            }
        }

    override suspend fun deleteMember(uid: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val statement = connection.prepareStatement(DELETE_MEMBER)
            statement.setString(1, uid)
            statement.executeUpdate()
            statement.close()
            return@withContext Result.success(true)
        } catch (e: Throwable) {
            return@withContext Result.failure(GenericException)
        }
    }

}