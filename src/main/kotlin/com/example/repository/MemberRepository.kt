package com.example.repository

import com.example.model.Member

interface MemberRepository {
    fun create(): String
    fun read(uid: String): Member
    fun updateName(uid: String, name: String)
    fun updateLastSeen(uid: String, chatId: String)
    fun delete(uid: String)
}