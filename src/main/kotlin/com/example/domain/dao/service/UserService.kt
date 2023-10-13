package com.example.domain.dao.service

import com.example.domain.model.User

interface UserService {
    suspend fun getUser(user: User): Result<User>
    suspend fun createUser(user: User): Result<Boolean>
    suspend fun updateUserUsername(username: String): Result<Boolean>
    suspend fun updateUserPassword(user: User): Result<Boolean>
    suspend fun deleteUser(username: String): Result<Boolean>
}