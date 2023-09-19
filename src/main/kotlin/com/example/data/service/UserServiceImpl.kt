package com.example.data.service

import com.example.data.model.User
import com.example.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    override suspend fun createUser(username: String, password: String) = withContext(Dispatchers.IO) {
        userRepository.createUser(username, password)
    }

    override suspend fun deleteUser(username: String) = withContext(Dispatchers.IO) {
        userRepository.deleteUser(username)
    }

    override suspend fun getUser(username: String): User = withContext(Dispatchers.IO) {
        userRepository.readUser(username)
    }

    override suspend fun updateUserPassword(password: String) = withContext(Dispatchers.IO) {
        userRepository.updateUserPassword(password)
    }

    override suspend fun updateUserUsername(username: String) = withContext(Dispatchers.IO) {
        userRepository.updateUserUsername(username)
    }
}