package com.example.socket

import com.example.model.ChatMember
import com.example.service.ChatService
import com.example.service.MemberService
import io.ktor.websocket.*

class ChatMemberSocketTestHandler(
    private val chatService: ChatService,
    private val memberService: MemberService
) : ChatMemberSocketHandler{
    override suspend fun joinChat(
        chatId: String,
        memberId: String,
        memberSession: DefaultWebSocketSession,
        onJoin: suspend (ChatMember) -> Unit
    ) {
        TODO("Not yet implemented")
    }
}