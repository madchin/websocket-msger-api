package com.example.data.repository

import com.example.data.dao.DatabaseFactory.dbQuery
import com.example.data.dao.model.Chat
import com.example.data.dao.table.Chats
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class ChatRepositoryImpl2 : ChatRepository {
    private fun resultRowToChat(row: ResultRow) = Chat(
        id = row[Chats.id].toString(),
        name = row[Chats.name],
        messageIds = row[Chats.messageIds].toList(),
        memberIds = row[Chats.memberIds].toList()
    )
    override suspend fun createChat(chat: Chat): Result<Chat> = dbQuery {
        val rowsAffected = Chats.insert {
            it[name] = chat.name
            it[memberIds] = chat.memberIds.toIntArray()
            it[messageIds] = chat.messageIds.toIntArray()
        }
        if (rowsAffected.insertedCount > 0) {
            Result.success(rowsAffected.resultedValues!!.single().let(::resultRowToChat))
        }
        Result.failure(Exception("Chat $chat not inserted"))
    }
    override suspend fun readChat(id: String): Result<Chat> = dbQuery {
        val chat = Chats
            .select { Chats.id eq UUID.fromString(id) }
            .map(::resultRowToChat)
            .singleOrNull()
        if (chat != null) {
            Result.success(chat)
        }
        Result.failure(NotFoundException("Chat with id $id not exists"))

    }
    override suspend fun updateChatName(id: String, name: String): Result<Boolean> = dbQuery {
        val updatedRows = Chats
            .update({ Chats.id eq UUID.fromString(id) }) {
                it[Chats.name] = name
            }
        if (updatedRows > 0) {
            Result.success(true)
        }
        Result.failure(NotFoundException("Chat with id $id not found"))

    }
    override suspend fun deleteChat(id: String): Result<Boolean> = dbQuery {
        val deletedRows = Chats
            .deleteWhere { Chats.id eq UUID.fromString(id) }
        if (deletedRows > 0) {
            Result.success(true)
        }
        Result.failure(NotFoundException("Chat with id $id not found"))

    }
}