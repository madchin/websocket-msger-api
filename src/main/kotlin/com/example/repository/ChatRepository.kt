package com.example.repository

import com.example.model.Chat

interface ChatRepository {
    fun create(chat: Chat): String
    fun read(id: String): Chat
    fun updateMembers(id: String, member: String)
    fun updateMessages(id: String, message: String)
    fun updateName(id: String, name: String)
    fun delete(id: String)
}