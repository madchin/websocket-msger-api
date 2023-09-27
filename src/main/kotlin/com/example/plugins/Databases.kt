package com.example.plugins

import com.example.data.RepositoriesImpl
import com.example.data.Services
import com.example.data.ServicesImpl
import com.example.data.dao.DatabaseFactory
import com.example.data.repository.*
import com.example.data.service.*
import com.example.data.util.addDatabaseExtensions
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.*
import java.sql.*

fun Application.configureDatabase() {
    //val dbConnection: Connection = connectToDatabase(embedded = false)
    //addDatabaseExtensions(dbConnection)

    DatabaseFactory.init(false, environment = environment)
}

/**
 * Makes a connection to a database.
 *
 * @param embedded -- if [true] defaults to an embedded database for tests that runs locally in the same process.
 * In this case you don't have to provide any parameters in configuration file, and you don't have to run a process.
 *
 * @return [Connection] that represent connection to the database. Please, don't forget to close this connection when
 * your application shuts down by calling [Connection.close]
 * */
fun Application.connectToDatabase(embedded: Boolean): Connection {
    val driverClassName = environment.config.property("database.driverClassName").getString()
    Class.forName(driverClassName)
    if (embedded) {
        return DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "root", "")
    } else {

        val url = environment.config.property("database.jdbcUrl").getString()
        val user = environment.config.property("database.user").getString()
        val password = environment.config.property("database.password").getString()

        return DriverManager.getConnection(url, user, password)
    }
}
