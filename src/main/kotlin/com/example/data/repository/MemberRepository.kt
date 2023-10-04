package com.example.data.repository

import com.example.data.model.Member

interface MemberRepository {
    suspend fun createMember(): String
    suspend fun readMember(uid: String): Member
    suspend fun updateMemberName(uid: String, name: String)
    suspend fun updateMemberLastSeen(uid: String, chatId: String)
    suspend fun deleteMember(uid: String)
}