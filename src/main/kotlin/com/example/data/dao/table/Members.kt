package com.example.data.dao.table

import org.jetbrains.exposed.sql.Table


object Members : Table() {
    val uid = reference("uid", Users)
    val name = text("name", eagerLoading = true)

    override val primaryKey = PrimaryKey(uid)
}