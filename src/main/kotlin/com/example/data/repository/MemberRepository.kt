package com.example.data.repository

import com.example.data.dao.model.Member

interface MemberRepository {
    suspend fun createMember(member: Member): Result<Member>
    suspend fun readMember(uid: String): Result<Member>
    suspend fun updateMemberName(uid: String, name: String): Result<Boolean>
    suspend fun deleteMember(uid: String): Result<Boolean>
}