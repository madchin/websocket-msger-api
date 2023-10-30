package com.example.service

import com.example.dao.repository.UserRepository
import com.example.model.User
import com.example.model.UserDTO
import com.example.util.ExplicitException
import com.example.util.PasswordHasher

private suspend fun ensureUserIsUnique(userRepository: UserRepository, username: String) {
    userRepository.readUser(username).getOrNull()?.let {
        throw ExplicitException.DuplicateUser
    }
}

private fun ensurePasswordIsCorrect(attemptUser: UserDTO, retrievedUser: User) {
    if (!PasswordHasher.checkPassword(attemptUser.password, retrievedUser.password)) {
        throw ExplicitException.WrongCredentials
    }
}

class AuthServiceImpl(private val userRepository: UserRepository) : AuthService {
    override suspend fun login(userDto: UserDTO): User =
        userRepository.readUser(userDto.username).getOrThrow().also { ensurePasswordIsCorrect(userDto, it) }

    override suspend fun register(userDto: UserDTO): User {
        ensureUserIsUnique(userRepository, userDto.username)
        val hashedPassword = PasswordHasher.hashPassword(userDto.password)
        val hashedUserDto = userDto.copy(password = hashedPassword)

        return userRepository.createUser(hashedUserDto.toUser()).getOrThrow()
    }
}