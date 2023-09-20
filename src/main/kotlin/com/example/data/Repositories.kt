package com.example.data

import com.example.data.repository.ChatRepositoryImpl
import com.example.data.repository.MemberRepositoryImpl
import com.example.data.repository.MessageRepositoryImpl
import com.example.data.repository.UserRepositoryImpl
import java.sql.Connection

class Repositories(dbConnection: Connection) {
    val chatRepository = ChatRepositoryImpl(dbConnection)
    val memberRepository = MemberRepositoryImpl(dbConnection)
    val messageRepository = MessageRepositoryImpl(dbConnection)
    val userRepository = UserRepositoryImpl(dbConnection)
}