package com.example.data.util

import java.sql.Connection

fun addDatabaseExtensions(connection: Connection) {
    val statement = connection.prepareStatement("CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\";")
    statement.executeUpdate()
    statement.close()
}