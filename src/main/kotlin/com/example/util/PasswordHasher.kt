package com.example.util

import org.mindrot.jbcrypt.BCrypt

object PasswordHasher {
    fun checkPassword(attempt: String, hashed: String) {
        val isPasswordCorrect = BCrypt.checkpw(attempt, hashed)
        if (!isPasswordCorrect) {
            throw ExplicitException.WrongCredentials
        }
    }

    fun hashPassword(password: String) = BCrypt.hashpw(password, BCrypt.gensalt())
}