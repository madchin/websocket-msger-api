package com.example.utils

import java.sql.Connection

fun ConfigureDatabase(connection: Connection) {
    val statement = connection.createStatement()
    val createUuidExt = "CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\";"
    statement.executeQuery(createUuidExt)
    statement.close()
}