package com.example.dao.repository

import com.example.model.User

interface UserRepository {
    suspend fun readUser(email: String): Result<User>
    suspend fun createUser(user: User): Result<User>
    suspend fun updateUserPassword(user: User): Result<Boolean>
    suspend fun deleteUser(email: String): Result<Boolean>
}