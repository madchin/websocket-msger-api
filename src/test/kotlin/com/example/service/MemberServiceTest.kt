package com.example.service

import com.example.TestConfig
import com.example.model.Member
import com.example.model.UserDTO
import com.example.util.ExplicitException
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.test.*

class MemberServiceTest : TestConfig() {
    @Test
    fun `Fail to add member which already exists`(): Unit = runBlocking {
        val user = ServiceFactory.authService.register(UserDTO("username", "email", "password"))
        ServiceFactory.memberService.addMember(Member(user.id!!, USERNAME))

        assertFailsWith<ExplicitException.DuplicateMember> {
            ServiceFactory.memberService.addMember(Member(user.id!!, USERNAME))
        }
    }

    @Test
    fun `Fail to add member when member uid is not in Users table`(): Unit = runBlocking {
        assertFailsWith<ExplicitException.UserNotFound> {
            ServiceFactory.memberService.addMember(Member(firstUserId, USERNAME))
        }
    }

    @Test
    fun `Successfully add member`(): Unit = runBlocking {
        val user = ServiceFactory.authService.register(UserDTO("username", "email", "password"))
        val member = ServiceFactory.memberService.addMember(Member(user.id!!, USERNAME))

        assertEquals(user.id, member.uid)
        assertEquals(USERNAME, member.name)
    }

    @Test
    fun `Fail to read member which not exists`(): Unit = runBlocking {
        assertFailsWith<ExplicitException.MemberNotFound> {
            ServiceFactory.memberService.getMember(firstUserId)
        }
    }

    @Test
    fun `Successfully read member`(): Unit = runBlocking {
        val user = ServiceFactory.authService.register(UserDTO("username", "email", "password"))
        val member = ServiceFactory.memberService.addMember(Member(user.id!!, USERNAME))
        val readMember = ServiceFactory.memberService.getMember(member.uid)

        assertEquals(member.uid, readMember.uid)
        assertEquals(member.name, readMember.name)
    }

    @Test
    fun `Fail to update member name when member not exists`(): Unit = runBlocking {
        assertFailsWith<ExplicitException.MemberNotFound> {
            ServiceFactory.memberService.updateMemberName(firstUserId, USERNAME_TO_UPDATE)
        }
    }

    @Test
    fun `Successfully update member name`(): Unit = runBlocking {
        val user = ServiceFactory.authService.register(UserDTO("username", "email", "password"))
        val member = ServiceFactory.memberService.addMember(Member(user.id!!, USERNAME))
        val updatedMember = ServiceFactory.memberService.updateMemberName(member.uid, USERNAME_TO_UPDATE)

        assertEquals(member.uid, updatedMember.uid)
        assertNotEquals(member.name, updatedMember.name)
        assertEquals(USERNAME_TO_UPDATE, updatedMember.name)
    }

    @Test
    fun `Fail to delete member which not exists`(): Unit = runBlocking {
        assertFailsWith<ExplicitException.MemberNotFound> {
            ServiceFactory.memberService.deleteMember(firstUserId)
        }
    }

    @Test
    fun `Successfully delete member`(): Unit = runBlocking {
        val user = ServiceFactory.authService.register(UserDTO("username", "email", "password"))
        val member = ServiceFactory.memberService.addMember(Member(user.id!!, USERNAME))
        val deleteResult = ServiceFactory.memberService.deleteMember(member.uid)
        assertTrue(deleteResult)
    }

    private companion object {
        const val USERNAME = "username"
        const val USERNAME_TO_UPDATE = "update_username"
        val firstUserId = UUID.randomUUID().toString()
    }

}