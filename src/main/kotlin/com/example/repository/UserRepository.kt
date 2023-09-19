package com.example.repository

import com.example.model.User

interface UserRepository {
    fun read(username: String): User
    fun insert(username: String, password: String)
    fun updateUsername(username: String)
    fun updatePassword(password: String)
    fun delete(username: String)
}