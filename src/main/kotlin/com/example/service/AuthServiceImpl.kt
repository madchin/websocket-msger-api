package com.example.service

import com.example.domain.dao.repository.UserRepository
import com.example.domain.model.User
import com.example.domain.model.UserDTO
import com.example.domain.service.AuthService
import com.example.util.WrongCredentialsException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.mindrot.jbcrypt.BCrypt

class AuthServiceImpl(private val userRepository: UserRepository) : AuthService {
    override suspend fun login(userDto: UserDTO): User =
        withContext(Dispatchers.IO) {
            val user = userRepository.readUser(userDto.username).getOrThrow()
            val isPasswordCorrect = BCrypt.checkpw(userDto.password, user.password)
            if(!isPasswordCorrect) {
                throw WrongCredentialsException
            }
            return@withContext user
        }

    override suspend fun register(userDto: UserDTO) =
        withContext(Dispatchers.IO) {
            val existingUser = userRepository.readUser(userDto.username).getOrNull()
            if(existingUser == null) {
                val hashedPassword = BCrypt.hashpw(userDto.password, BCrypt.gensalt())
                val hashedUserDto = userDto.copy(password = hashedPassword)
                userRepository.createUser(hashedUserDto.toUser()).getOrThrow()
            }
        }

}