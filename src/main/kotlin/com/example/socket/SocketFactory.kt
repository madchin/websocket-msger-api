package com.example.socket

import com.example.service.ChatService
import com.example.service.MemberService

object SocketFactory {
    lateinit var chatRoom: ChatRoomSocketHandler
    lateinit var chatMember: ChatMemberSocketHandler

    fun init(chatService: ChatService, memberService: MemberService) {
        chatRoom = ChatRoomSocketHandlerImpl(chatService)
        chatMember = ChatMemberSocketHandlerImpl(chatService, memberService)
    }
}