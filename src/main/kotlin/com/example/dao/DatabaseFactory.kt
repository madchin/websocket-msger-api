package com.example.dao

import com.example.dao.table.Chats
import com.example.dao.table.Members
import com.example.dao.table.Messages
import com.example.dao.table.Users
import io.ktor.server.application.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(embedded: Boolean, environment: ApplicationEnvironment) {
        fun connectToDb(): Lazy<Database> = if (embedded) {
            lazy {
                Database.connect(
                    url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
                    driver = "org.h2.Driver",
                    user = "root",
                    password = ""
                )
            }
        } else {
            val url = environment.config.property("database.jdbcUrl").getString()
            val user = environment.config.property("database.user").getString()
            val password = environment.config.property("database.password").getString()
            val driverClassName = environment.config.property("database.driverClassName").getString()

            lazy {
                Database.connect(url = url, driver = driverClassName, user = user, password = password)
            }
        }
        fun createSchemas(database: Database) {
            transaction(database) {
                SchemaUtils.create(Chats)
                SchemaUtils.create(Users)
                SchemaUtils.create(Members)
                SchemaUtils.create(Messages)
            }
        }
        fun configureLoggers(database: Database) {
            transaction(database) {
                // add logger print sql to stdout
                addLogger(StdOutSqlLogger)
            }
        }
        val databaseConnection by connectToDb()
        configureLoggers(databaseConnection)
        createSchemas(databaseConnection)
    }
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
    suspend fun <T> dbAsyncQuery(block: suspend () -> T, database: Database): Deferred<T> =
        suspendedTransactionAsync(Dispatchers.IO, database) { block() }
}