package com.example.domain.dao.service

import com.example.domain.model.Chat

interface ChatService {
    suspend fun createChat(chat: Chat): Chat
    suspend fun getChat(chatId: String, memberId: String): Chat
    suspend fun changeChatName(id: String, name: String): Boolean
    suspend fun deleteChat(id: String): Boolean
    suspend fun joinChat(chatId: String, memberUid: String): Chat
}