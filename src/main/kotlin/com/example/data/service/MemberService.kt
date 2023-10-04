package com.example.data.service

import com.example.data.dao.model.Member

interface MemberService {
    suspend fun createMember(member: Member): Result<Member>
    suspend fun getMember(uid: String): Result<Member>
    suspend fun updateMemberName(uid: String, name: String): Result<Boolean>
    suspend fun deleteMember(uid: String): Result<Boolean>
}