package com.example.data.service

import com.example.data.repository.Repositories

class Services(private val repositories: Repositories) {
    val chatService = ChatServiceImpl(repositories.chatRepository)
    val userService = UserServiceImpl(repositories.userRepository)
    val messageService = MessageServiceImpl(repositories.messageRepository)
    val memberService = MemberServiceImpl(repositories.memberRepository)
}