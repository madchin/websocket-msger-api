package com.example.models

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.sql.Connection

data class User(val username: String, val password: String)

class UserService(private val connection: Connection) {

    companion object {
        private val CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS users (" +
                "uid UUID DEFAULT uuid_generate_v4() PRIMARY KEY, " +
                "username varchar(64), " +
                "password varchar(128)" +
                ");"
        private val SELECT_USER_BY_USERNAME = "SELECT (username, password) FROM users WHERE username = ?"
        private val INSERT_USER = "INSERT INTO users (username, password) VALUES (?, ?);"
        private val UPDATE_USERNAME = "UPDATE users SET username = ? WHERE uid = ?"
        private val UPDATE_PASSWORD = "UPDATE users SET password = ? where uid = ?"
        private val DELETE_USER = "DELETE FROM users WHERE username = ?"
    }

    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_USER)
        statement.close()
    }
    suspend fun read(username: String): User = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_USER_BY_USERNAME)
        statement.setString(1, username)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val password = resultSet.getString("password")

            statement.close()
            return@withContext User(username = username, password = password)
        } else {
            statement.close()
            throw Exception("Record not found")
        }
    }

    suspend fun insert(username: String, password: String) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_USER)
        statement.setString(1, username)
        statement.setString(2, password)
        statement.executeUpdate()
        statement.close()
    }
    suspend fun updateUsername(username: String) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_USERNAME)
        statement.setString(1, username)
        statement.executeUpdate()
        statement.close()
    }

    suspend fun updatePassword(password: String) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_PASSWORD)
        statement.setString(1, password)
        statement.executeUpdate()
        statement.close()
    }
    suspend fun delete(username: String) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_USER)
        statement.setString(1, username)
        statement.executeUpdate()
        statement.close()
    }

}