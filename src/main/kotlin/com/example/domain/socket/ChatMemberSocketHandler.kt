package com.example.domain.socket

import com.example.domain.model.Member

interface ChatMemberSocketHandler {
    suspend fun joinChat(chatId: String, memberId: String): Member

}