package com.example.data.dao.table

import com.example.util.EntityFieldLength
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp

object Messages : IntIdTable() {
    val chatId = reference("chat_id", Chats)
    val sender = varchar("sender", EntityFieldLength.Messages.Sender.maxLength)
    val content = text("content", eagerLoading = true)
    val timestamp = timestamp("timestamp")
}