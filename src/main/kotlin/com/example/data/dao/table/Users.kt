package com.example.data.dao.table

import org.jetbrains.exposed.dao.id.UUIDTable

object Users : UUIDTable() {
    val username = varchar("username",64)
    val email = varchar("email",320)
    val password = varchar("password",11111)
}