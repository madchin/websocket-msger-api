package com.example.data

import com.example.data.service.*

interface Services {
    val chatService: ChatService
    val userService: UserService
    val messageService: MessageService
    val memberService: MemberService
}