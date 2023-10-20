package com.example.socket

import com.example.model.ChatMember
import com.example.service.ChatService
import com.example.service.MemberService
import com.example.model.Member
import io.ktor.websocket.*

class ChatMemberSocketHandlerImpl(
    private val chatService: ChatService,
    private val memberService: MemberService
) : ChatMemberSocketHandler {
    override suspend fun joinChat(chatId: String, memberId: String, memberSession: DefaultWebSocketSession, onJoin: suspend (ChatMember) -> Unit) {
        val member = memberService.getMember(memberId)
        chatService.joinChat(chatId,memberId)

        onJoin(member.toChatMember(memberSession))
    }
}