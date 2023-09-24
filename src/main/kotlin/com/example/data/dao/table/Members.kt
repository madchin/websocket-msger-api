package com.example.data.dao.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.json.jsonb

object Members : IntIdTable() {
    val uid = reference("uid", Users)
    val name = text("name")
    val lastSeen = jsonb("last_seen", { it.toString() }, {
        val regex = Regex("\\b[a-zA-Z]+\\b")
        val result = regex.findAll(it).map { it.value }.toList()
        Pair(result[0], result[1])
    })
}