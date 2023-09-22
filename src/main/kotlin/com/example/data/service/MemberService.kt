package com.example.data.service

import com.example.data.model.Member

interface MemberService {
    suspend fun createMember(): Result<String>
    suspend fun getMember(uid: String): Result<Member>
    suspend fun updateMemberName(uid: String, name: String): Result<Boolean>
    suspend fun updateMemberLastSeen(uid: String, chatId: String): Result<Boolean>
    suspend fun deleteMember(uid: String): Result<Boolean>
}