package com.example.service

import com.example.domain.dao.repository.UserRepository
import com.example.domain.model.User
import com.example.domain.model.UserDTO
import com.example.domain.service.AuthService
import com.example.util.ExplicitException
import com.example.util.PasswordHasher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync

class AuthServiceImpl(private val userRepository: UserRepository) : AuthService {
    override suspend fun login(userDto: UserDTO): User =
        withContext(Dispatchers.IO) {
            val user = userRepository.readUser(userDto.username).getOrThrow()
            PasswordHasher.checkPassword(userDto.password, user.password)

            return@withContext user
        }

    override suspend fun register(userDto: UserDTO): Unit =
        withContext(Dispatchers.IO) {
            userRepository.readUser(userDto.username).getOrNull()?.let {
                throw ExplicitException.DuplicateUser
            }
            val hashedPassword = PasswordHasher.hashPassword(userDto.password)
            val hashedUserDto = userDto.copy(password = hashedPassword)
            userRepository.createUser(hashedUserDto.toUser()).getOrThrow()
        }

}