package com.example.data

import com.example.data.service.ChatServiceImpl
import com.example.data.service.MemberServiceImpl
import com.example.data.service.MessageServiceImpl
import com.example.data.service.UserServiceImpl

class Services(repositories: Repositories) {
    val chatService = ChatServiceImpl(repositories.chatRepository)
    val userService = UserServiceImpl(repositories.userRepository)
    val messageService = MessageServiceImpl(repositories.messageRepository)
    val memberService = MemberServiceImpl(repositories.memberRepository)
}