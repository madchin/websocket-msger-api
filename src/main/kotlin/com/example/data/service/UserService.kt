package com.example.data.service

import com.example.data.model.User

interface UserService {
    suspend fun getUser(username: String): Result<User>
    suspend fun createUser(username: String, password: String): Result<Boolean>
    suspend fun updateUserUsername(username: String): Result<Boolean>
    suspend fun updateUserPassword(password: String): Result<Boolean>
    suspend fun deleteUser(username: String): Result<Boolean>
}