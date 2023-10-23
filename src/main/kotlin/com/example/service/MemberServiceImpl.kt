package com.example.service

import com.example.dao.repository.MemberRepository
import com.example.model.Member

class MemberServiceImpl(private val memberRepository: MemberRepository) : MemberService {
    override suspend fun addMember(member: Member): Member {
        return memberRepository.insertMember(member).getOrThrow()
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