package com.example.data.socket

import com.example.data.util.GenericException
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
    override suspend fun sendMessage(message: Message): Result<Boolean> =
        messageService.sendMessage(message)

    override suspend fun joinChat(chatId: String, memberId: String): Result<Pair<Member,Chat>> {
        val getMemberAndChatResult = memberChatService.getMemberAndChat(memberId = memberId, chatId = chatId)
        return when {
            getMemberAndChatResult.isSuccess -> {
                val chatJoinResult = chatService.joinChat(chatId = chatId, memberUid = memberId)
                if(chatJoinResult.isSuccess) {
                    return Result.success(getMemberAndChatResult.getOrNull()!!)
                }
                return Result.failure(chatJoinResult.exceptionOrNull()!!)
            }
            getMemberAndChatResult.isFailure -> Result.failure(getMemberAndChatResult.exceptionOrNull()!!)
            else -> Result.failure(GenericException())
        }
    }
}