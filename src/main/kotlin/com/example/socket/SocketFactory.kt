package com.example.socket

import com.example.service.ChatService
import com.example.service.MemberService

object SocketFactory {
    lateinit var chatRoomHandler: ChatRoomSocketHandler

    fun init(chatService: ChatService, memberService: MemberService) {
        chatRoomHandler = ChatRoomSocketHandlerImpl(chatService, memberService)
    }
}