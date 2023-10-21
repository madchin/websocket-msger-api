package com.example.service

import com.example.dao.repository.MemberRepository
import com.example.model.Member

class MemberTestService(
    private val memberRepository: MemberRepository
) : MemberService {
    override suspend fun createOrUpdateMember(member: Member): Member {
        TODO("Not yet implemented")
    }

    override suspend fun getMember(uid: String): Member {
        TODO("Not yet implemented")
    }

    override suspend fun updateMemberName(uid: String, name: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMember(uid: String): Boolean {
        TODO("Not yet implemented")
    }
}