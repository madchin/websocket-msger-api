package com.example.service

import com.example.domain.dao.repository.UserRepository
import com.example.domain.model.User
import com.example.domain.model.UserDTO
import com.example.domain.service.AuthService
import com.example.util.ExplicitException
import com.example.util.PasswordHasher

class AuthServiceImpl(private val userRepository: UserRepository) : AuthService {
    override suspend fun login(userDto: UserDTO): User =
        userRepository.readUser(userDto.username).getOrThrow().also {
            PasswordHasher.checkPassword(userDto.password, it.password)
        }

    override suspend fun register(userDto: UserDTO) {
        userRepository.readUser(userDto.username).getOrNull()?.let {
            throw ExplicitException.DuplicateUser
        }
        val hashedPassword = PasswordHasher.hashPassword(userDto.password)
        val hashedUserDto = userDto.copy(password = hashedPassword)

        userRepository.createUser(hashedUserDto.toUser()).getOrThrow()
    }
}