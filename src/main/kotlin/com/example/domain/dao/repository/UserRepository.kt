package com.example.domain.dao.repository

import com.example.domain.model.User

interface UserRepository {
    suspend fun readUser(username: String): Result<User>
    suspend fun createUser(user: User): Result<User>
    suspend fun updateUserUsername(username: String): Result<Boolean>
    suspend fun updateUserPassword(user: User): Result<Boolean>
    suspend fun deleteUser(username: String): Result<Boolean>
}