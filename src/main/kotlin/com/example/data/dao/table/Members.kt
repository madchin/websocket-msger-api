package com.example.data.dao.table

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.json.json

object Members : IntIdTable() {
    val uid = reference("uid", Users)
    val name = text("name", eagerLoading = true)
}