package com.example.data.dao.repository

import com.example.data.dao.DatabaseFactory.dbQuery
import com.example.domain.model.Chat
import com.example.data.dao.table.Chats
import com.example.util.InsertionException
import com.example.util.UpdateException
import com.example.domain.dao.repository.ChatRepository
import com.example.util.ChatNotFoundException
import io.ktor.server.plugins.*
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
            return@dbQuery Result.failure(InsertionException("Chat with name ${chat.name} not inserted"))
        }
    }

    override suspend fun readChat(chatId: String): Result<Chat> = dbQuery {
        Chats
            .select { Chats.id eq UUID.fromString(chatId) }
            .map(::resultRowToChat)
            .singleOrNull()?.let {
                return@dbQuery Result.success(it)
            }
        return@dbQuery Result.failure(ChatNotFoundException("Chat with id $chatId not found"))
    }

    override suspend fun updateChatName(chatId: String, name: String): Result<Boolean> = dbQuery {
        Chats
            .update({ Chats.id eq UUID.fromString(chatId) }) {
                it[Chats.name] = name
            }.let {
                if (it != 0) {
                    return@dbQuery Result.success(true)
                }
                return@dbQuery Result.failure(NotFoundException("Chat with id $chatId not found"))
            }
    }

    override suspend fun updateChatLastSeenMembers(chatId: String, memberUid: String): Result<Chat> =
        dbQuery {
            val chat = Chats.select { Chats.id eq UUID.fromString(chatId) }
                .map(::resultRowToChat)
                .singleOrNull()

            chat?.lastSeenMembers?.filterNot { it.keys.contains(memberUid) }?.let { filteredLastSeen ->
                val lastSeenTimestamp = System.currentTimeMillis()
                Chats.update {
                    it[lastSeenMembers] = filteredLastSeen + mapOf(memberUid to lastSeenTimestamp)
                }.run {
                    if (this != 0) {
                        return@dbQuery Result.success(chat)
                    }
                    return@dbQuery Result.failure(UpdateException("Chat with id $chatId field has not been updated"))
                }
            }
            return@dbQuery Result.failure(NotFoundException("Chat with id $chatId has not been found"))
        }

    override suspend fun deleteChat(chatId: String): Result<Boolean> = dbQuery {
        Chats
            .deleteWhere { Chats.id eq UUID.fromString(chatId) }
            .let {
                if (it != 0) {
                    return@dbQuery Result.success(true)
                }
                return@dbQuery Result.failure(NotFoundException("Chat with id $chatId not found"))
            }
    }
}