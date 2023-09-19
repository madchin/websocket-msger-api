package com.example.repository

import com.example.model.User

interface UserRepository {
    suspend fun read(username: String): User
    suspend fun create(username: String, password: String)
    suspend fun updateUsername(username: String)
    suspend fun updatePassword(password: String)
    suspend fun delete(username: String)
}