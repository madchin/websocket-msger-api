package com.example.data.service

import com.example.data.model.Member

interface MemberService {
    suspend fun createMember(): String
    suspend fun getMember(uid: String): Member
    suspend fun updateMemberName(uid: String, name: String)
    suspend fun updateMemberLastSeen(uid: String, chatId: String)
    suspend fun deleteMember(uid: String)
}