package com.example.dao.repository

import com.example.model.User

class UserTestRepository : UserRepository {
    override suspend fun readUser(username: String): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun createUser(user: User): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserUsername(username: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserPassword(user: User): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(username: String): Result<Boolean> {
        TODO("Not yet implemented")
    }
}