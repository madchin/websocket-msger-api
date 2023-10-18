package com.example.domain.service

import com.example.domain.model.User

interface UserService {
    suspend fun getUser(user: User): User
    suspend fun createUser(user: User): Boolean
    suspend fun updateUserUsername(username: String): Boolean
    suspend fun updateUserPassword(user: User): Boolean
    suspend fun deleteUser(username: String): Boolean
}