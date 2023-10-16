package com.example.data.dao.service

import com.example.domain.model.Member
import com.example.domain.dao.repository.MemberRepository
import com.example.domain.dao.service.MemberService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MemberServiceImpl(private val memberRepository: MemberRepository) : MemberService {
    override suspend fun createOrUpdateMember(member: Member): Member = withContext(Dispatchers.IO) {
        return@withContext memberRepository.upsertMember(member).getOrThrow()
    }

    override suspend fun deleteMember(uid: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext memberRepository.deleteMember(uid).getOrThrow()
    }

    override suspend fun getMember(uid: String): Member = withContext(Dispatchers.IO) {
        return@withContext memberRepository.readMember(uid).getOrThrow()
    }

    override suspend fun updateMemberName(uid: String, name: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext memberRepository.updateMemberName(uid, name).getOrThrow()
    }
}