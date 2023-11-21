package com.example.dao.table

import com.example.util.EntityFieldLength
import org.jetbrains.exposed.dao.id.UUIDTable

object Users : UUIDTable() {
    val email = varchar("email", EntityFieldLength.Users.Email.maxLength).uniqueIndex()
    val password = varchar("password", EntityFieldLength.Users.Password.maxLength)
}