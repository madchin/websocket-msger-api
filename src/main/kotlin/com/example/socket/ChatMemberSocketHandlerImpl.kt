package com.example.socket

import com.example.service.ChatService
import com.example.service.MemberService
import com.example.model.Member

class ChatMemberSocketHandlerImpl(
    private val chatService: ChatService,
    private val memberService: MemberService
) : ChatMemberSocketHandler {
    override suspend fun joinChat(chatId: String, memberId: String): Member {
        val member = memberService.getMember(memberId)
        chatService.joinChat(chatId,memberId)

        return member
    }
}