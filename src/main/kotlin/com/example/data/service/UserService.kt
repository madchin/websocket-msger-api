package com.example.data.service

import com.example.data.model.User

interface UserService {
    suspend fun getUser(username: String): User
    suspend fun createUser(username: String, password: String)
    suspend fun updateUserUsername(username: String)
    suspend fun updateUserPassword(password: String)
    suspend fun deleteUser(username: String)
}