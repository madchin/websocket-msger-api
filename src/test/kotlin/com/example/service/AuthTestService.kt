package com.example.service

import com.example.dao.repository.UserRepository
import com.example.model.User
import com.example.model.UserDTO

class AuthTestService(
    private val userRepository: UserRepository
) : AuthService {
    override suspend fun login(userDto: UserDTO): User {
        TODO("Not yet implemented")
    }

    override suspend fun register(userDto: UserDTO) {
        TODO("Not yet implemented")
    }
}