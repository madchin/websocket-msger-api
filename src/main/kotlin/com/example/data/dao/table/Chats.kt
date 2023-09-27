package com.example.data.dao.table

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.json.json


object Chats : UUIDTable() {
    val name = varchar("name", 255)
    val memberIds = json<IntArray>("member_ids", Json.Default)
    val messageIds = json<IntArray>("message_ids", Json.Default)
}