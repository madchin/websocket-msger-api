package com.example.data.dao

import com.example.data.dao.repository.ChatRepositoryImpl
import com.example.data.dao.repository.MemberRepositoryImpl
import com.example.data.dao.repository.MessageRepositoryImpl
import com.example.data.dao.repository.UserRepositoryImpl
import com.example.domain.dao.Repositories

class RepositoriesImpl : Repositories {
    override val chatRepository = ChatRepositoryImpl()
    override val userRepository = UserRepositoryImpl()
    override val memberRepository = MemberRepositoryImpl()
    override val messageRepository = MessageRepositoryImpl()
}