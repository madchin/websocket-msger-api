package com.example.domain.service

interface Services {
    val chatService: ChatService
    val userService: UserService
    val messageService: MessageService
    val memberService: MemberService
    val authService: AuthService
}