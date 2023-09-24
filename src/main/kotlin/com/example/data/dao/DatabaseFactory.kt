package com.example.data.dao

import io.ktor.server.engine.*
import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    fun init(embedded: Boolean, environment: ApplicationEngineEnvironment): Lazy<Database> {
        return if (embedded) {
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
    }
}