package com.example.service

import com.example.model.Member

interface MemberService {
    suspend fun addMember(member: Member): Member
    suspend fun getMember(uid: String): Member
    suspend fun updateMemberName(uid: String, name: String): Member
    suspend fun deleteMember(uid: String): Boolean
}