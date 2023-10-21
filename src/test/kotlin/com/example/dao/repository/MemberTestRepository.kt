package com.example.dao.repository

import com.example.model.Member

class MemberTestRepository : MemberRepository {
    override suspend fun upsertMember(member: Member): Result<Member> {
        TODO("Not yet implemented")
    }

    override suspend fun readMember(uid: String): Result<Member> {
        TODO("Not yet implemented")
    }

    override suspend fun updateMemberName(uid: String, name: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMember(uid: String): Result<Boolean> {
        TODO("Not yet implemented")
    }
}