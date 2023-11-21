package com.example.dao.repository

import com.example.dao.DatabaseFactory.dbQuery
import com.example.dao.table.Users
import com.example.model.User
import com.example.util.ExplicitException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserRepositoryImpl : UserRepository {
    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id].toString(),
        email = row[Users.email],
        password = row[Users.password]
    )

    override suspend fun readUser(email: String): Result<User> = dbQuery {
        Users
            .select { Users.email eq email }
            .map(::resultRowToUser)
            .singleOrNull()
            .let {
                if (it != null) {
                    return@dbQuery Result.success(it)
                }
                return@dbQuery Result.failure(ExplicitException.UserNotFound)
            }
    }

    override suspend fun createUser(user: User): Result<User> = dbQuery {
        Users.insert {
            it[email] = user.email
            it[password] = user.password
        }.run {
            val insertedUser = resultedValues?.singleOrNull()?.let(::resultRowToUser)
            if (insertedUser != null) {
                return@dbQuery Result.success(insertedUser)
            }
            return@dbQuery Result.failure(ExplicitException.UserInsert)
        }
    }

    override suspend fun updateUserPassword(user: User): Result<Boolean> = dbQuery {
        Users.update({ Users.email eq user.email }) {
            it[password] = user.password
        }.let {
            if (it != 0) {
                return@dbQuery Result.success(true)
            }
            return@dbQuery Result.failure(ExplicitException.UserNotFound)
        }
    }

    override suspend fun deleteUser(email: String): Result<Boolean> = dbQuery {
        Users
            .deleteWhere { Users.email eq email }
            .let {
                if (it != 0) {
                    return@dbQuery Result.success(true)
                }
                return@dbQuery Result.failure(ExplicitException.UserNotFound)
            }
    }
}