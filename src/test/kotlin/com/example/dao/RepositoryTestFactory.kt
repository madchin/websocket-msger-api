package com.example.dao

import com.example.dao.repository.*

object RepositoryTestFactory {
    lateinit var chatRepository: ChatRepository
    lateinit var userRepository: UserRepository
    lateinit var memberRepository: MemberRepository
    lateinit var messageRepository: MessageRepository

    fun init() {
        chatRepository = ChatTestRepository()
        userRepository = UserTestRepository()
        memberRepository = MemberTestRepository()
        messageRepository = MessageTestRepository()
    }
}