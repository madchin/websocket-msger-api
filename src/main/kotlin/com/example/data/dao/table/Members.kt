package com.example.data.dao.table

import com.example.util.EntityFieldLength
import org.jetbrains.exposed.sql.Table


object Members : Table() {
    val uid = reference("uid", Users)
    val name = varchar("name", EntityFieldLength.MEMBERS_NAME.maxLength)

    override val primaryKey = PrimaryKey(uid)
}