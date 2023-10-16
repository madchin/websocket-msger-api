package com.example.data.dao.service

import com.example.data.util.GenericException
import com.example.domain.dao.service.ChatService
import com.example.domain.dao.service.MemberChatService
import com.example.domain.dao.service.MemberService
import com.example.domain.model.Chat
import com.example.domain.model.Member
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class MemberChatServiceImpl(private val memberService: MemberService, private val chatService: ChatService) : MemberChatService {
    override suspend fun getMemberAndChat(chatId: String, memberId: String): Pair<Member,Chat> = withContext(Dispatchers.IO) {
        val getMemberAsync = async { memberService.getMember(memberId) }
        val getChatAsync = async { chatService.getChat(chatId) }

        awaitAll(getMemberAsync, getChatAsync).run {
            val member = this[0] as Member
            val chat = this[1] as Chat
            return@withContext Pair(member, chat)
        }
    }
}