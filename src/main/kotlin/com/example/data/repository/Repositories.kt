package com.example.data.repository

import java.sql.Connection

class Repositories(private val dbConnection: Connection) {
    val chatRepository = ChatRepositoryImpl(dbConnection)
    val memberRepository = MemberRepositoryImpl(dbConnection)
    val messageRepository = MessageRepositoryImpl(dbConnection)
    val userRepository = UserRepositoryImpl(dbConnection)
}