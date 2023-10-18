package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(val id: String?, val username: String, val email: String, val password: String)
@Serializable
data class UserDTO(val username: String, val email: String, val password: String) {
    fun toUser(): User = User(id = null, username = username, email = email, password = password)
}


