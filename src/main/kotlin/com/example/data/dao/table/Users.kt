package com.example.data.dao.table

import com.example.data.util.EntityFieldLength
import org.jetbrains.exposed.dao.id.UUIDTable
object Users : UUIDTable() {
    val username = varchar("username",EntityFieldLength.USERS_USERNAME.maxLength)
    val email = varchar("email",EntityFieldLength.USERS_EMAIL.maxLength)
    val password = varchar("password",EntityFieldLength.USERS_PASSWORD.maxLength)
}