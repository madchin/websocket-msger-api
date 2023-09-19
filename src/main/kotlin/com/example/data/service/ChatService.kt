package com.example.data.service

import com.example.data.model.Chat

interface ChatService {
    suspend fun createChat(chat: Chat): String
    suspend fun getChat(id: String): Chat
    suspend fun changeChatName(id: String, name: String)
    suspend fun deleteChat(id: String)
}