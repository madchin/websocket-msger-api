package com.example.data.dao

import com.example.data.dao.table.Chats
import com.example.data.dao.table.Members
import com.example.data.dao.table.Messages
import com.example.data.dao.table.Users
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(embedded: Boolean, environment: ApplicationEnvironment): Database {
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
        val databaseConnection by connectToDb()
        createSchemas(databaseConnection)

        return databaseConnection
    }
}