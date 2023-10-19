package com.example.data.dao.table

import com.example.util.EntityFieldLength
import org.jetbrains.exposed.dao.id.UUIDTable

object Users : UUIDTable() {
    val username = varchar("username", EntityFieldLength.Users.Username.maxLength).uniqueIndex()
    val email = varchar("email", EntityFieldLength.Users.Email.maxLength)
    val password = varchar("password", EntityFieldLength.Users.Password.maxLength)
}