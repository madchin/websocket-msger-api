package com.example.data.dao.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.json.jsonb

object Members : Table() {
    val uid = reference("id", Users)
    val name = text("name")
    val lastSeen = jsonb("lastSeen", { it.toString() }, {
        val regex = Regex("\\b[a-zA-Z]+\\b")
        val result = regex.findAll(it).map { it.value }.toList()
        Pair(result[0], result[1])
    })
}