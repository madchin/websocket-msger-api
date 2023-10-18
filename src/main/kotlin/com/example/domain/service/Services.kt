package com.example.domain.service

interface Services {
    val chatService: ChatService
    val messageService: MessageService
    val memberService: MemberService
    val authService: AuthService
}