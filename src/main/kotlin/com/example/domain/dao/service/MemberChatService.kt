package com.example.domain.dao.service

interface MemberChatService {
    suspend fun canJoinChat(chatId: String, memberId: String): Result<Boolean>
}