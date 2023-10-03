package com.example.data.repository

import com.example.data.dao.DatabaseFactory.dbQuery
import com.example.data.dao.model.Message
import com.example.data.dao.table.Messages
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import java.sql.Timestamp
import java.util.*

class MessageRepositoryImpl2 : MessageRepository {

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
            it[timestamp] = Timestamp(message.timestamp).toInstant()
        }.run {
            val insertedMessage = resultedValues?.singleOrNull()?.let(::resultRowToMessage)
            if(insertedMessage != null) {
                return@dbQuery Result.success(true)
            }
            return@dbQuery Result.failure(Exception("Message from sender ${message.sender} with content ${message.content} and with timestamp ${message.timestamp} has not been sent"))
        }
    }

    override suspend fun readMessages(chatId: String): Result<List<Message>> = dbQuery {
        Messages.select { Messages.chatId eq UUID.fromString(chatId) }.map(::resultRowToMessage).run {
            if(this.isNotEmpty()) {
                return@dbQuery Result.success(this)
            }
            return@dbQuery Result.failure(NotFoundException("Messages in chat with id $chatId has not been found"))
        }
    }
}