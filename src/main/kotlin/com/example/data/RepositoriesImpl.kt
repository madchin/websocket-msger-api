package com.example.data

import com.example.data.repository.ChatRepositoryImpl2
import com.example.data.repository.MemberRepositoryImpl
import com.example.data.repository.MessageRepositoryImpl
import com.example.data.repository.UserRepositoryImpl
import java.sql.Connection

class RepositoriesImpl : Repositories {
    override val chatRepository = ChatRepositoryImpl2()
//    override val userRepository = UserRepositoryImpl(dbConnection)
//    override val memberRepository = MemberRepositoryImpl(dbConnection)
//    override val messageRepository = MessageRepositoryImpl(dbConnection)
}