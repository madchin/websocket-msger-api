package com.example.data.dao.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.json.json

object Chats : UUIDTable() {
    val name = varchar("name", 255)
    val members = json("members", { it.toString() }, {
        //FIXME
        listOf(it.toInt())
    })
    val messages = json("messages", { it.toString() }, {
        //FIXME
        listOf(it.toInt())
    })
}