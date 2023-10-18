package com.example.domain.dao

import com.example.domain.dao.service.*

interface Services {
    val chatService: ChatService
    val userService: UserService
    val messageService: MessageService
    val memberService: MemberService
}