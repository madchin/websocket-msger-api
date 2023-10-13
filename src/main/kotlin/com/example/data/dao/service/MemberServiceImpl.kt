package com.example.data.dao.service

import com.example.domain.model.Member
import com.example.domain.dao.repository.MemberRepository
import com.example.domain.dao.service.MemberService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MemberServiceImpl(private val memberRepository: MemberRepository) : MemberService {
    override suspend fun createOrUpdateMember(member: Member): Result<Member> = withContext(Dispatchers.IO) {
        return@withContext memberRepository.upsertMember(member)
    }

    override suspend fun deleteMember(uid: String): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext memberRepository.deleteMember(uid)
    }

    override suspend fun getMember(uid: String): Result<Member> = withContext(Dispatchers.IO) {
        return@withContext memberRepository.readMember(uid)
    }

    override suspend fun updateMemberName(uid: String, name: String): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext memberRepository.updateMemberName(uid, name)
    }
}