package com.example.data.dao.table

import org.jetbrains.exposed.dao.id.UUIDTable

object Chats : UUIDTable() {
    val name = varchar("name", 255)
}