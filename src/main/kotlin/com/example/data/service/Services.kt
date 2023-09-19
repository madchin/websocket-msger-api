package com.example.data.service

import com.example.data.repository.ChatRepository
import com.example.data.repository.MemberRepository
import com.example.data.repository.MessageRepository
import com.example.data.repository.UserRepository

class Services(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository,
    private val memberRepository: MemberRepository
) {
    val chatService = ChatServiceImpl(chatRepository)
    val userService = UserServiceImpl(userRepository)
    val messageService = MessageServiceImpl(messageRepository)
    val memberService = MemberServiceImpl(memberRepository)
}