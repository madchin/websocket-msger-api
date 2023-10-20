package com.example.dao

import com.example.dao.repository.*

object RepositoryFactory {
    lateinit var chatRepository: ChatRepository
    lateinit var userRepository: UserRepository
    lateinit var memberRepository: MemberRepository
    lateinit var messageRepository: MessageRepository

    fun init() {
        chatRepository = ChatRepositoryImpl()
        userRepository = UserRepositoryImpl()
        memberRepository = MemberRepositoryImpl()
        messageRepository = MessageRepositoryImpl()
    }
}