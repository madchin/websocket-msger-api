package com.example.service

import com.example.domain.model.User
import com.example.domain.dao.repository.UserRepository
import com.example.domain.service.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    override suspend fun createUser(user: User): Boolean = withContext(Dispatchers.IO) {
        return@withContext userRepository.createUser(user).getOrThrow()
    }

    override suspend fun deleteUser(username: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext userRepository.deleteUser(username).getOrThrow()
    }

    override suspend fun getUser(user: User): User = withContext(Dispatchers.IO) {
        return@withContext userRepository.readUser(user.username).getOrThrow()
    }

    override suspend fun updateUserPassword(user: User): Boolean = withContext(Dispatchers.IO) {
        return@withContext userRepository.updateUserPassword(user).getOrThrow()
    }

    override suspend fun updateUserUsername(username: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext userRepository.updateUserUsername(username).getOrThrow()
    }
}