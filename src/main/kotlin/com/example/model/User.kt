package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class User(val id: String?, val email: String, val password: String) {
    fun toUserDTO(): UserDTO = UserDTO(password = this.password, email = this.email)
}
@Serializable
data class UserDTO(val email: String, val password: String) {
    fun toUser(): User = User(id = null, email = email, password = password)
}


