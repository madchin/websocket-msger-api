package com.example.data.repository

import com.example.data.dao.DatabaseFactory.dbQuery
import com.example.data.dao.model.Chat
import com.example.data.dao.table.Chats
import com.example.data.util.GenericException
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class ChatRepositoryImpl2 : ChatRepository {
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
            return@dbQuery Result.failure(Exception("Chat with name ${chat.name} not inserted"))
        }
    }

    override suspend fun readChat(id: String): Result<Chat> = dbQuery {
        val chat = Chats
            .select { Chats.id eq UUID.fromString(id) }
            .map(::resultRowToChat)
            .singleOrNull()
        if (chat != null) {
            return@dbQuery Result.success(chat)
        }
        return@dbQuery Result.failure(NotFoundException("Chat with id $id not found"))

    }

    override suspend fun updateChatName(id: String, name: String): Result<Boolean> = dbQuery {
        Chats
            .update({ Chats.id eq UUID.fromString(id) }) {
                it[Chats.name] = name
            }.let {
                if (it != 0) {
                    return@dbQuery Result.success(true)
                }
                return@dbQuery Result.failure(NotFoundException("Chat with id $id not found"))
            }
    }

    override suspend fun updateChatLastSeenMembers(chatId: String, memberUid: String, lastSeen: Long): Result<Boolean> =
        dbQuery {
            val chatLastSeen = Chats.slice(Chats.lastSeenMembers).select { Chats.id eq UUID.fromString(chatId) }
                .map { it[Chats.lastSeenMembers] }
                .singleOrNull()

            chatLastSeen?.filterNot { it.keys.contains(memberUid) }?.let { filteredLastSeen ->
                Chats.update {
                    it[lastSeenMembers] = filteredLastSeen + mapOf(memberUid to lastSeen)
                }.run {
                    if (this != 0) {
                        return@dbQuery Result.success(true)
                    }
                    return@dbQuery Result.failure(NotFoundException("Chat with id $chatId has not been found"))
                }
            }
            return@dbQuery Result.failure(GenericException())
        }

    override suspend fun deleteChat(id: String): Result<Boolean> = dbQuery {
        Chats
            .deleteWhere { Chats.id eq UUID.fromString(id) }
            .let {
                if (it != 0) {
                    return@dbQuery Result.success(true)
                }
                return@dbQuery Result.failure(NotFoundException("Chat with id $id not found"))
            }
    }
}