package com.example.service

import com.example.dao.repository.UserRepository
import com.example.model.User
import com.example.model.UserDTO
import com.example.util.ExplicitException
import com.example.util.PasswordHasher

class AuthServiceImpl(private val userRepository: UserRepository) : AuthService {

    private suspend fun ensureUserIsUnique(username: String) {
        userRepository.readUser(username).getOrNull()?.let {
            throw ExplicitException.DuplicateUser
        }
    }
    override suspend fun login(userDto: UserDTO): User =
        userRepository.readUser(userDto.username).getOrThrow().also {
            PasswordHasher.checkPassword(userDto.password, it.password)
        }

    override suspend fun register(userDto: UserDTO) {
        ensureUserIsUnique(userDto.username)
        val hashedPassword = PasswordHasher.hashPassword(userDto.password)
        val hashedUserDto = userDto.copy(password = hashedPassword)

        userRepository.createUser(hashedUserDto.toUser()).getOrThrow()
    }
}