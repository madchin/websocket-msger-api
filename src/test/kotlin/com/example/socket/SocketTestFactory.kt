package com.example.socket

import com.example.service.ChatService
import com.example.service.MemberService

object SocketTestFactory {
    lateinit var chatRoomHandler: ChatRoomSocketHandler
    lateinit var chatMemberHandler: ChatMemberSocketHandler

    fun init(chatService: ChatService, memberService: MemberService) {
        chatRoomHandler = ChatRoomSocketTestHandler(chatService)
        chatMemberHandler = ChatMemberSocketTestHandler(chatService, memberService)
    }
}