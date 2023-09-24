package com.example.data.service

import com.example.data.dao.model.User
import com.example.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    override suspend fun createUser(username: String, password: String): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext userRepository.createUser(username, password)
    }

    override suspend fun deleteUser(username: String): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext userRepository.deleteUser(username)
    }

    override suspend fun getUser(username: String): Result<User> = withContext(Dispatchers.IO) {
        return@withContext userRepository.readUser(username)
    }

    override suspend fun updateUserPassword(password: String): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext userRepository.updateUserPassword(password)
    }

    override suspend fun updateUserUsername(username: String): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext userRepository.updateUserUsername(username)
    }
}