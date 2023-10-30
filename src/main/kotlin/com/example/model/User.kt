package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class User(val id: String?, val username: String, val email: String, val password: String) {
    fun toUserDTO(): UserDTO = UserDTO(username = this.username, password = this.password, email = this.email)
}
@Serializable
data class UserDTO(val username: String, val email: String, val password: String) {
    fun toUser(): User = User(id = null, username = username, email = email, password = password)
}


