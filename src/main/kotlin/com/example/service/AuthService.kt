package com.example.service

import com.example.model.User
import com.example.model.UserDTO

interface AuthService {
    suspend fun login(userDto: UserDTO): User
    suspend fun register(userDto: UserDTO)

}