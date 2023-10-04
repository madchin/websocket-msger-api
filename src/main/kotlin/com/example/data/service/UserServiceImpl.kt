package com.example.data.service

import com.example.data.dao.model.User
import com.example.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    override suspend fun createUser(user: User): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext userRepository.createUser(user)
    }

    override suspend fun deleteUser(username: String): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext userRepository.deleteUser(username)
    }

    override suspend fun getUser(user: User): Result<User> = withContext(Dispatchers.IO) {
        return@withContext userRepository.readUser(user)
    }

    override suspend fun updateUserPassword(user: User): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext userRepository.updateUserPassword(user)
    }

    override suspend fun updateUserUsername(username: String): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext userRepository.updateUserUsername(username)
    }
}