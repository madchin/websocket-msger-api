package com.example.data.dao.service

import com.example.data.util.GenericException
import com.example.domain.dao.service.ChatService
import com.example.domain.dao.service.MemberChatService
import com.example.domain.dao.service.MemberService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class MemberChatServiceImpl(private val memberService: MemberService, private val chatService: ChatService) : MemberChatService {
    override suspend fun canJoinChat(chatId: String, memberId: String): Result<Boolean> = withContext(Dispatchers.IO) {
        val getMemberAsync = async { memberService.getMember(memberId) }
        val getChatAsync = async { chatService.getChat(chatId) }
        val memberResult = getMemberAsync.await()
        val chatResult = getChatAsync.await()

        return@withContext when {
            memberResult.isSuccess && chatResult.isSuccess -> Result.success(true)
            memberResult.isFailure -> Result.failure(memberResult.exceptionOrNull()!!)
            chatResult.isFailure -> Result.failure(chatResult.exceptionOrNull()!!)
            else -> Result.failure(GenericException())
        }
    }
}