package com.example.data.repository

import com.example.data.dao.DatabaseFactory.dbQuery
import com.example.data.dao.model.Chat
import com.example.data.dao.table.Chats
import com.example.data.util.GenericException
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import java.sql.SQLException

//        private const val CREATE_TABLE_CHATS =
//            "CREATE TABLE IF NOT EXISTS chats (" +
//                    "id UUID DEFAULT uuid_generate_v4() PRIMARY KEY, " +
//                    "name VARCHAR(255)" +
//                        "members JSON, "
//                            "messages JSON, "
//                    ");"
//
//        private const val INSERT_CHAT = "INSERT INTO chats (name) VALUES (?);"
//
//        private const val SELECT_CHAT_BY_ID = "SELECT id, name FROM chats WHERE id = ?;"
//
//        private const val UPDATE_CHAT_NAME = "UPDATE chats SET name = ? WHERE id = ?;"
//
//        private const val DELETE_CHAT = "DELETE FROM chats WHERE id = ?;"
class ChatRepositoryImpl2 : ChatRepository {

    private fun resultRowToChat(row: ResultRow) = Chat(
        id = row[Chats.id].toString(),
        name = row[Chats.name],
        messagesIds = row[Chats.messages],
        membersIds = row[Chats.members]
    )
    override suspend fun createChat(chat: Chat): Result<Chat> = dbQuery {

        val insertedStatement = Chats.insert {
            it[Chats.name] = chat.name
            it[Chats.members] = chat.membersIds
            it[Chats.messages] = chat.messagesIds
        }
        if(insertedStatement.insertedCount == 0) {
            return@dbQuery Result.failure(Exception("Not inserted"))
        }
        return@dbQuery Result.success(insertedStatement.resultedValues!!.single().let(::resultRowToChat))

    }

    override suspend fun readChat(id: String): Result<Chat> {
        TODO("Not yet implemented")
    }

    override suspend fun updateChatName(id: String, name: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteChat(id: String): Result<Boolean> {
        TODO("Not yet implemented")
    }
}