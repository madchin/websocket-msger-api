package com.example.dao.repository

import com.example.dao.DatabaseFactory.dbQuery
import com.example.dao.table.Messages
import com.example.model.Message
import com.example.util.ExplicitException
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.sql.Timestamp
import java.util.*

class MessageRepositoryImpl : MessageRepository {

    private fun resultRowToMessage(row: ResultRow): Message = Message(
        chatId = row[Messages.chatId].toString(),
        sender = row[Messages.sender],
        content = row[Messages.content],
        timestamp = Timestamp.from(row[Messages.timestamp]).time
    )

    override suspend fun createMessage(message: Message): Result<Boolean> = dbQuery {
        Messages.insert {
            it[chatId] = UUID.fromString(message.chatId)
            it[content] = message.content
            it[sender] = message.sender
            it[timestamp] = Timestamp(message.timestamp ?: 0).toInstant()
        }.run {
            val insertedMessage = resultedValues?.singleOrNull()?.let(::resultRowToMessage)
            if (insertedMessage != null) {
                return@dbQuery Result.success(true)
            }
            return@dbQuery Result.failure(ExplicitException.MessageInsert)
        }
    }

    override suspend fun readMessages(chatId: String): Result<List<Message>> = dbQuery {
        Messages.select { Messages.chatId eq UUID.fromString(chatId) }.map(::resultRowToMessage).run {
            if (this.isNotEmpty()) {
                return@dbQuery Result.success(this)
            }
            return@dbQuery Result.failure(ExplicitException.MessagesNotFound)
        }
    }
}