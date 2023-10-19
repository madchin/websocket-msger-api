package com.example.service

import com.example.domain.dao.repository.MemberRepository
import com.example.domain.model.Member
import com.example.domain.service.MemberService

class MemberServiceImpl(private val memberRepository: MemberRepository) : MemberService {
    override suspend fun createOrUpdateMember(member: Member): Member {
        return memberRepository.upsertMember(member).getOrThrow()
    }

    override suspend fun deleteMember(uid: String): Boolean {
        return memberRepository.deleteMember(uid).getOrThrow()
    }

    override suspend fun getMember(uid: String): Member {
        return memberRepository.readMember(uid).getOrThrow()
    }

    override suspend fun updateMemberName(uid: String, name: String): Boolean {
        return memberRepository.updateMemberName(uid, name).getOrThrow()
    }
}