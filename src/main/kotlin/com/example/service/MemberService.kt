package com.example.service

import com.example.model.Member

interface MemberService {
    suspend fun createOrUpdateMember(member: Member): Member
    suspend fun getMember(uid: String): Member
    suspend fun updateMemberName(uid: String, name: String): Boolean
    suspend fun deleteMember(uid: String): Boolean
}