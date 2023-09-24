package com.example.data.service

import com.example.data.dao.model.Member
import com.example.data.repository.MemberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MemberServiceImpl(private val memberRepository: MemberRepository) : MemberService {
    override suspend fun createMember(): Result<String> = withContext(Dispatchers.IO) {
        return@withContext memberRepository.createMember()
    }

    override suspend fun deleteMember(uid: String): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext memberRepository.deleteMember(uid)
    }

    override suspend fun getMember(uid: String): Result<Member> = withContext(Dispatchers.IO) {
        return@withContext memberRepository.readMember(uid)
    }

    override suspend fun updateMemberLastSeen(uid: String, chatId: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            return@withContext memberRepository.updateMemberLastSeen(uid, chatId)

        }

    override suspend fun updateMemberName(uid: String, name: String): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext memberRepository.updateMemberName(uid, name)
    }
}