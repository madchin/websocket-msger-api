package com.example.data.repository

import com.example.data.dao.model.User
import com.example.data.util.GenericException
import com.example.data.util.parseUserData
import io.ktor.server.plugins.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection

class UserRepositoryImpl(private val connection: Connection) : UserRepository {
    companion object {
        private val CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS users (" +
                "uid UUID DEFAULT uuid_generate_v4() PRIMARY KEY, " +
                "username varchar(64), " +
                "password varchar(128)" +
                ");"
        private val SELECT_USER_BY_USERNAME = "SELECT (username, password) FROM users WHERE username = ?;"
        private val INSERT_USER = "INSERT INTO users (username, password) VALUES (?, ?)" // ON CONFLICT (username) DO NOTHING;
        private val UPDATE_USERNAME = "UPDATE users SET username = ? WHERE uid = ?"
        private val UPDATE_PASSWORD = "UPDATE users SET password = ? where uid = ?"
        private val DELETE_USER = "DELETE FROM users WHERE username = ?"
    }

    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_USER)
        statement.close()
    }

    override suspend fun readUser(username: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val statement = connection.prepareStatement(SELECT_USER_BY_USERNAME)
            statement.setString(1, username)
            val resultSet = statement.executeQuery()

            if (resultSet.next()) {
                val userData = resultSet.getString(1)
                val (_, password) = parseUserData(userData)
                return@withContext Result.success(User(username, password))
            }
            return@withContext Result.failure(NotFoundException("User with $username username not found"))

        } catch (e: Throwable) {
            return@withContext Result.failure(GenericException())
        }
    }

    override suspend fun createUser(username: String, password: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val statement = connection.prepareStatement(INSERT_USER)
            statement.setString(1, username)
            statement.setString(2, password)
            //statement.setString(3, username)
            val rowsAffected = statement.executeUpdate()
            statement.close()
            //return@withContext if(rowsAffected == 1) Result.success(true) else Result.failure(Exception("User with $username already exists"))
            return@withContext Result.success(true)
        } catch (e: Throwable) {
            return@withContext Result.failure(GenericException())
        }
        TODO("fix not adding data when username / email already exists")
    }

    override suspend fun updateUserUsername(username: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val statement = connection.prepareStatement(UPDATE_USERNAME)
            statement.setString(1, username)
            statement.executeUpdate()
            statement.close()
            return@withContext Result.success(true)
        } catch (e: Throwable) {
            return@withContext Result.failure(GenericException())
        }
    }

    override suspend fun updateUserPassword(password: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val statement = connection.prepareStatement(UPDATE_PASSWORD)
            statement.setString(1, password)
            statement.executeUpdate()
            statement.close()
            return@withContext Result.success(true)
        } catch (e: Throwable) {
            return@withContext Result.failure(GenericException())
        }
    }

    override suspend fun deleteUser(username: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val statement = connection.prepareStatement(DELETE_USER)
            statement.setString(1, username)
            statement.executeUpdate()
            statement.close()
            return@withContext Result.success(true)
        } catch (e: Throwable) {
            return@withContext Result.failure(GenericException())
        }
    }

}