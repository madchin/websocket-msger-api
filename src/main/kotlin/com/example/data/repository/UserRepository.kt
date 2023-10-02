package com.example.data.repository

import com.example.data.dao.model.User

interface UserRepository {
    suspend fun readUser(user: User): Result<User>
    suspend fun createUser(user: User): Result<Boolean>
    suspend fun updateUserUsername(username: String): Result<Boolean>
    suspend fun updateUserPassword(user: User): Result<Boolean>
    suspend fun deleteUser(username: String): Result<Boolean>
}