package com.example.data.dao.table

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.json.json


object Chats : UUIDTable() {
    val name = varchar("name", 255)
    val lastSeenMembers = json<List<Map<String, Long>>>("last_seen_members", Json.Default)
    val messageIds = json<IntArray>("message_ids", Json.Default)
}