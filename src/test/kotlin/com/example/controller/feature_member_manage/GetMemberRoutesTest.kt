package com.example.controller.feature_member_manage

import com.example.TestConfig
import com.example.controller.test_util.testApp
import com.example.controller.util.JwtConfig
import com.example.model.Member
import com.example.model.UserDTO
import com.example.service.ServiceFactory
import io.ktor.client.request.*
import io.ktor.http.*
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class GetMemberRoutesTest : TestConfig() {

    @Test
    fun `Unauthorized - fail to get member`() = testApp(false) { client ->
        client.get("/member").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `Authorized - Fail to get member when there is no member with current logged in user id`() = testApp { client ->
        client.get("/member") {
            val token = JwtConfig.createToken(firstUserId)
            bearerAuth(token)
        }.apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun `Authorized - Successfully get member when member exists`() = testApp { client ->
        val registeredUser = ServiceFactory.authService.register(UserDTO("username", "email", "password"))
        ServiceFactory.memberService.addMember(Member(registeredUser.id!!, "memberName"))
        client.get("/member") {
            val token = JwtConfig.createToken(registeredUser.id!!)
            bearerAuth(token)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    private companion object {
        val firstUserId = UUID.randomUUID().toString()
    }
}