package com.example.data.dao.repository

import com.example.data.dao.DatabaseFactory.dbQuery
import com.example.data.dao.table.Members
import com.example.domain.dao.repository.MemberRepository
import com.example.domain.model.Member
import com.example.util.ExplicitException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class MemberRepositoryImpl : MemberRepository {
    private fun resultRowToMember(row: ResultRow): Member = Member(
        uid = row[Members.uid].toString(),
        name = row[Members.name]
    )

    override suspend fun upsertMember(member: Member): Result<Member> = dbQuery {
        Members.upsert {
            it[uid] = UUID.fromString(member.uid)
            it[name] = member.name
        }.run {
            val insertedMember = resultedValues?.singleOrNull()?.let(::resultRowToMember)
            if (insertedMember != null) {
                return@dbQuery Result.success(insertedMember)
            }
            return@dbQuery Result.failure(ExplicitException.MemberUpsert)
        }
    }

    override suspend fun readMember(uid: String): Result<Member> = dbQuery {
        Members
            .select { Members.uid eq UUID.fromString(uid) }
            .map(::resultRowToMember)
            .singleOrNull()
            .let {
                if (it != null) {
                    return@dbQuery Result.success(it)
                }
                return@dbQuery Result.failure(ExplicitException.MemberNotFound)
            }
    }

    override suspend fun updateMemberName(uid: String, name: String): Result<Boolean> = dbQuery {
        Members
            .update({ Members.uid eq UUID.fromString(uid) }) {
                it[Members.name] = name
            }.let {
                if (it != 0) {
                    return@dbQuery Result.success(true)
                }
                return@dbQuery Result.failure(ExplicitException.MemberUpdate)
            }
    }

    override suspend fun deleteMember(uid: String): Result<Boolean> = dbQuery {
        Members
            .deleteWhere { Members.uid eq UUID.fromString(uid) }.let {
                if (it != 0) {
                    return@dbQuery Result.success(true)
                }
                return@dbQuery Result.failure(ExplicitException.MemberNotFound)
            }
    }
}