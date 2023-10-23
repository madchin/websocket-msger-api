package com.example.dao.repository

import com.example.dao.DatabaseFactory.dbQuery
import com.example.dao.table.Chats
import com.example.model.Chat
import com.example.util.ExplicitException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class ChatRepositoryImpl : ChatRepository {
    private fun resultRowToChat(row: ResultRow) = Chat(
        id = row[Chats.id].toString(),
        name = row[Chats.name],
        messageIds = row[Chats.messageIds].toList(),
        lastSeenMembers = row[Chats.lastSeenMembers]
    )

    override suspend fun createChat(chat: Chat): Result<Chat> = dbQuery {
        Chats.insert {
            it[name] = chat.name
            it[lastSeenMembers] = chat.lastSeenMembers
            it[messageIds] = chat.messageIds.toIntArray()
        }.run {
            val insertedChat = resultedValues?.singleOrNull()?.let(::resultRowToChat)
            if (insertedChat != null) {
                return@dbQuery Result.success(insertedChat)
            }
            return@dbQuery Result.failure(ExplicitException.ChatInsert)
        }
    }

    override suspend fun readChat(chatId: String): Result<Chat> = dbQuery {
        Chats
            .select { Chats.id eq UUID.fromString(chatId) }
            .map(::resultRowToChat)
            .singleOrNull()?.let {
                return@dbQuery Result.success(it)
            }
        return@dbQuery Result.failure(ExplicitException.ChatNotFound)
    }

    override suspend fun updateChatName(chatId: String, name: String): Result<Chat> = dbQuery {
        Chats
            .update({ Chats.id eq UUID.fromString(chatId) }) {
                it[Chats.name] = name
            }.let {
                if (it != 0) {
                    val updatedChat = Chats.select { Chats.id eq UUID.fromString(chatId) }.singleOrNull()?.let(::resultRowToChat)
                    return@dbQuery Result.success(updatedChat!!)
                }
                return@dbQuery Result.failure(ExplicitException.ChatNotFound)
            }
    }

    override suspend fun updateChatLastSeenMembers(chat: Chat, memberUid: String): Result<Chat> =
        dbQuery {
            val lastSeenTimestamp = System.currentTimeMillis()
            Chats.update {
                it[lastSeenMembers] = chat.lastSeenMembers + mapOf(memberUid to lastSeenTimestamp)
            }.run {
                if (this != 0) {
                    val updatedChat = Chats.select { Chats.id eq UUID.fromString(chat.id) }.singleOrNull()?.let(::resultRowToChat)
                    return@dbQuery Result.success(updatedChat!!)
                }
                return@dbQuery Result.failure(ExplicitException.ChatNotFound)
            }
        }

    override suspend fun deleteChat(chatId: String): Result<Boolean> = dbQuery {
        Chats
            .deleteWhere { Chats.id eq UUID.fromString(chatId) }
            .let {
                if (it != 0) {
                    return@dbQuery Result.success(true)
                }
                return@dbQuery Result.failure(ExplicitException.ChatNotFound)
            }
    }
}