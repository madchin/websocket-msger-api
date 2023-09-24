package com.example.data.dao.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object Messages : Table() {
    private val id = integer("id").autoIncrement()
    val chatId = reference("id", Chats)
    val sender = text("sender")
    val content = text("content")
    val timestamp = timestamp("timestamp")

    override val primaryKey = PrimaryKey(id)
}