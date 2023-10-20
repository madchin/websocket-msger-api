package com.example.service

import com.example.dao.repository.ChatRepository
import com.example.dao.repository.MemberRepository
import com.example.dao.repository.MessageRepository
import com.example.dao.repository.UserRepository

object ServiceFactory {
    lateinit var chatService: ChatService
    lateinit var memberService: MemberService
    lateinit var authService: AuthService
    fun init(
        chatRepository: ChatRepository,
        messageRepository: MessageRepository,
        memberRepository: MemberRepository,
        userRepository: UserRepository
    ) {
        chatService = ChatServiceImpl(chatRepository, messageRepository)
        memberService = MemberServiceImpl(memberRepository)
        authService = AuthServiceImpl(userRepository)
    }
}