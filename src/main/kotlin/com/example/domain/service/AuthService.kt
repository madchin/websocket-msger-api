package com.example.domain.service

import com.example.domain.model.User
import com.example.domain.model.UserDTO

interface AuthService {
    suspend fun login(userDto: UserDTO): User
    suspend fun register(userDto: UserDTO)

}