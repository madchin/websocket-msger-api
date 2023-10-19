package com.example.util

import org.mindrot.jbcrypt.BCrypt

object PasswordHasher {
    fun checkPassword(attempt:String, hashed: String) = BCrypt.checkpw(attempt, hashed)

    fun hashPassword(password: String) = BCrypt.hashpw(password, BCrypt.gensalt())
}