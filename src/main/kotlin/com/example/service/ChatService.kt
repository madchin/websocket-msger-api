package com.example.service

import com.example.model.Chat

interface ChatService {
    suspend fun createChat(chat: Chat): String
    suspend fun getChat(id: String): Chat
    suspend fun addChatMember(id: String, member: String)
    suspend fun addChatMessage(id: String, message: String)
    suspend fun changeChatName(id: String, name: String)
    suspend fun deleteChat(id: String)
}