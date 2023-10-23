package com.example.service

import com.example.dao.repository.MemberRepository
import com.example.model.Member
import com.example.util.ExplicitException

class MemberServiceImpl(private val memberRepository: MemberRepository) : MemberService {
    override suspend fun addMember(member: Member, currentUserId: String): Member {
        if(member.uid != currentUserId) throw ExplicitException.Forbidden
        return memberRepository.insertMember(member).getOrThrow()
    }

    override suspend fun deleteMember(uid: String): Boolean =
         memberRepository.deleteMember(uid).getOrThrow()


    override suspend fun getMember(uid: String): Member =
         memberRepository.readMember(uid).getOrThrow()


    override suspend fun updateMemberName(uid: String, name: String): Member =
         memberRepository.updateMemberName(uid, name).getOrThrow()

}