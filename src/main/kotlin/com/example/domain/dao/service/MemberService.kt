package com.example.domain.dao.service

import com.example.domain.model.Member

interface MemberService {
    suspend fun createOrUpdateMember(member: Member): Result<Member>
    suspend fun getMember(uid: String): Result<Member>
    suspend fun updateMemberName(uid: String, name: String): Result<Boolean>
    suspend fun deleteMember(uid: String): Result<Boolean>
}