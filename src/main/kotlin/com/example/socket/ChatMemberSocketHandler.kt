package com.example.socket

import com.example.model.Member

interface ChatMemberSocketHandler {
    suspend fun joinChat(chatId: String, memberId: String): Member

}