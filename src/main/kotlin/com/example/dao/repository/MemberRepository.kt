package com.example.dao.repository

import com.example.model.Member

interface MemberRepository {
    suspend fun insertMember(member: Member): Result<Member>
    suspend fun readMember(uid: String): Result<Member>
    suspend fun updateMemberName(uid: String, name: String): Result<Boolean>
    suspend fun deleteMember(uid: String): Result<Boolean>
}