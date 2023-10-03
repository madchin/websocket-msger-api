package com.example.data

import com.example.data.repository.*
import java.sql.Connection

class RepositoriesImpl : Repositories {
    override val chatRepository = ChatRepositoryImpl2()
    override val userRepository = UserRepositoryImpl2()
    override val memberRepository = MemberRepositoryImpl2()
    override val messageRepository = MessageRepositoryImpl2()
}