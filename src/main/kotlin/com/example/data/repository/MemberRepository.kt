package com.example.data.repository

import com.example.data.dao.model.Member

interface MemberRepository {
    suspend fun createMember(): Result<String>
    suspend fun readMember(uid: String): Result<Member>
    suspend fun updateMemberName(uid: String, name: String): Result<Boolean>
    suspend fun updateMemberLastSeen(uid: String, chatId: String): Result<Boolean>
    suspend fun deleteMember(uid: String): Result<Boolean>
}