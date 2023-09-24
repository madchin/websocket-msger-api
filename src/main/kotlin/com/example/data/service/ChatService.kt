package com.example.data.service

import com.example.data.dao.model.Chat

interface ChatService {
    suspend fun createChat(chat: Chat): Result<String>
    suspend fun getChat(id: String): Result<Chat>
    suspend fun changeChatName(id: String, name: String): Result<Boolean>
    suspend fun deleteChat(id: String): Result<Boolean>
}