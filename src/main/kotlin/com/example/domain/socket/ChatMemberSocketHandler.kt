package com.example.domain.socket

import com.example.domain.model.Chat
import com.example.domain.model.Member
import com.example.domain.model.Message
interface ChatMemberSocketHandler {
    suspend fun sendMessage(message: Message): Boolean

    suspend fun joinChat(chatId: String, memberId: String): Pair<Member,Chat>

}