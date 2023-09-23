package com.example.data.repository

import com.example.data.model.User

interface UserRepository {
    suspend fun readUser(username: String): Result<User>
    suspend fun createUser(username: String, password: String): Result<Boolean>
    suspend fun updateUserUsername(username: String): Result<Boolean>
    suspend fun updateUserPassword(password: String): Result<Boolean>
    suspend fun deleteUser(username: String): Result<Boolean>
}