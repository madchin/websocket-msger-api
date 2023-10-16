package com.example.domain.dao.service

import com.example.domain.model.Chat
import com.example.domain.model.Member

interface MemberChatService {
    suspend fun getMemberAndChat(chatId: String, memberId: String): Pair<Member,Chat>
}