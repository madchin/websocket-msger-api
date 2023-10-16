package com.example.data.socket

import com.example.util.GenericException
import com.example.domain.dao.service.ChatService
import com.example.domain.dao.service.MemberChatService
import com.example.domain.dao.service.MessageService
import com.example.domain.model.Chat
import com.example.domain.model.Member
import com.example.domain.model.Message
import com.example.domain.socket.ChatMemberSocketHandler

class ChatMemberSocketHandlerImpl(
    private val chatService: ChatService,
    private val memberChatService: MemberChatService,
    private val messageService: MessageService
) : ChatMemberSocketHandler {
    override suspend fun sendMessage(message: Message): Boolean =
        messageService.saveMessage(message)

    override suspend fun joinChat(chatId: String, memberId: String): Pair<Member, Chat> {
        val memberAndChat = memberChatService.getMemberAndChat(memberId = memberId, chatId = chatId)
        chatService.joinChat(chatId = chatId, memberUid = memberId)

        return memberAndChat
    }
}