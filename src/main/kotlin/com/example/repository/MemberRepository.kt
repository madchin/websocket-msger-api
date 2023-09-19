package com.example.repository

import com.example.model.Member

interface MemberRepository {
    suspend fun create(): String
    suspend fun read(uid: String): Member
    suspend fun updateName(uid: String, name: String)
    suspend fun updateLastSeen(uid: String, chatId: String)
    suspend fun delete(uid: String)
}