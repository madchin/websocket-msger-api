package com.example.service

import com.example.model.Member
import com.example.repository.MemberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MemberServiceImpl(private val memberRepository: MemberRepository) : MemberService {
    override suspend fun createMember(): String = withContext(Dispatchers.IO) {
        memberRepository.createMember()
    }

    override suspend fun deleteMember(uid: String) = withContext(Dispatchers.IO) {
        memberRepository.deleteMember(uid)
    }

    override suspend fun getMember(uid: String): Member = withContext(Dispatchers.IO) {
        memberRepository.readMember(uid)
    }

    override suspend fun updateMemberLastSeen(uid: String, chatId: String) = withContext(Dispatchers.IO) {
        memberRepository.updateMemberLastSeen(uid, chatId)
    }

    override suspend fun updateMemberName(uid: String, name: String) = withContext(Dispatchers.IO) {
        memberRepository.updateMemberName(uid, name)
    }
}