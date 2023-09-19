package com.example.repository

import com.example.model.User

interface UserRepository {
    suspend fun readUser(username: String): User
    suspend fun createUser(username: String, password: String)
    suspend fun updateUserUsername(username: String)
    suspend fun updateUserPassword(password: String)
    suspend fun deleteUser(username: String)
}