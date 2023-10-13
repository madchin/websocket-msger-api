package com.example.data.dao.repository

import com.example.data.dao.DatabaseFactory.dbQuery
import com.example.data.dao.table.Users
import com.example.domain.dao.repository.UserRepository
import com.example.domain.model.User
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserRepositoryImpl : UserRepository {
    private fun resultRowToUser(row: ResultRow) = User(
        username = row[Users.username],
        email = row[Users.email],
        password = row[Users.password]
    )

    override suspend fun readUser(user: User): Result<User> = dbQuery {
        Users
            .select { Users.username eq user.username and (Users.password eq user.password) }
            .map(::resultRowToUser)
            .singleOrNull()
            .let {
                if (it != null) {
                    return@dbQuery Result.success(it)
                }
                return@dbQuery Result.failure(NotFoundException("User with username ${user.username} not found."))
            }
    }

    override suspend fun createUser(user: User): Result<Boolean> = dbQuery {
        Users.insert {
            it[username] = user.username
            it[email] = user.email
            it[password] = user.password
        }.run {
            val insertedUser = resultedValues?.singleOrNull()?.let(::resultRowToUser)
            if (insertedUser != null) {
                return@dbQuery Result.success(true)
            }
            return@dbQuery Result.failure(Exception("User with username ${user.username} has not been created."))
        }
    }

    override suspend fun updateUserUsername(username: String): Result<Boolean> = dbQuery {
        Users.update({ Users.username eq username }) {
            it[Users.username] = username
        }.let {
            if (it != 0) {
                return@dbQuery Result.success(true)
            }
            return@dbQuery Result.failure(NotFoundException("User with username $username has not been found."))
        }
    }

    override suspend fun updateUserPassword(user: User): Result<Boolean> = dbQuery {
        Users.update({ Users.username eq user.username }) {
            it[username] = user.username
        }.let {
            if (it != 0) {
                return@dbQuery Result.success(true)
            }
            return@dbQuery Result.failure(NotFoundException("User with username ${user.username} has not been found."))
        }
    }

    override suspend fun deleteUser(username: String): Result<Boolean> = dbQuery {
        Users
            .deleteWhere { Users.username eq username }
            .let {
                if (it != 0) {
                    return@dbQuery Result.success(true)
                }
                return@dbQuery Result.failure(NotFoundException("User with username $username has not been found."))
            }
    }
}