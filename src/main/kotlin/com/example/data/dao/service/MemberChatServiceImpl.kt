package com.example.data.dao.service

import com.example.data.util.GenericException
import com.example.domain.dao.service.ChatService
import com.example.domain.dao.service.MemberChatService
import com.example.domain.dao.service.MemberService
import com.example.domain.model.Chat
import com.example.domain.model.Member
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class MemberChatServiceImpl(private val memberService: MemberService, private val chatService: ChatService) : MemberChatService {
    override suspend fun getMemberAndChat(chatId: String, memberId: String): Result<Pair<Member,Chat>> = withContext(Dispatchers.IO) {
        val getMemberAsync = async { memberService.getMember(memberId) }
        val getChatAsync = async { chatService.getChat(chatId) }
        val memberResult = getMemberAsync.await()
        val chatResult = getChatAsync.await()

        return@withContext when {
            memberResult.isSuccess && chatResult.isSuccess -> {
                val member = memberResult.getOrNull()!!
                val chat = chatResult.getOrNull()!!
                Result.success(member to chat)
            }
            memberResult.isFailure -> Result.failure(memberResult.exceptionOrNull()!!)
            chatResult.isFailure -> Result.failure(chatResult.exceptionOrNull()!!)
            else -> Result.failure(GenericException())
        }
    }
}