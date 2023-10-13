package com.example.data.dao.service

import com.example.domain.model.User
import com.example.domain.dao.repository.UserRepository
import com.example.domain.dao.service.UserService
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