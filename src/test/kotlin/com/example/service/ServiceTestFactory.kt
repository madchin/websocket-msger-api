package com.example.service

import com.example.dao.repository.ChatRepository
import com.example.dao.repository.MemberRepository
import com.example.dao.repository.MessageRepository
import com.example.dao.repository.UserRepository

object ServiceTestFactory {
    lateinit var chatService: ChatService
    lateinit var memberService: MemberService
    lateinit var authService: AuthService
    fun init(
        chatRepository: ChatRepository,
        messageRepository: MessageRepository,
        memberRepository: MemberRepository,
        userRepository: UserRepository
    ) {
        chatService = ChatTestService(chatRepository, messageRepository)
        memberService = MemberTestService(memberRepository)
        authService = AuthTestService(userRepository)
    }
}