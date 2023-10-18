package com.example.data.socket

import com.example.domain.service.ChatService
import com.example.domain.service.MemberService
import com.example.domain.model.Member
import com.example.domain.socket.ChatMemberSocketHandler

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