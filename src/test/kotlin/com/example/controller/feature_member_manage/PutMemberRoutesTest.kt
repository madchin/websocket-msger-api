package com.example.controller.feature_member_manage

import com.example.TestConfig
import com.example.controller.test_util.testApp
import com.example.controller.util.ErrorResponse
import com.example.controller.util.JwtConfig
import com.example.model.Member
import com.example.model.MemberDTO
import com.example.model.UserDTO
import com.example.service.ServiceFactory
import com.example.util.ExplicitException
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PutMemberRoutesTest : TestConfig() {
    @Test
    fun `Unauthorized - fail to update member name`() = testApp(false) { client ->
        client.put("/member/update-member-name").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
            body<ErrorResponse>().apply {
                assertEquals(
                    ErrorResponse(
                        ExplicitException.Unauthorized.description,
                        ExplicitException.Unauthorized.message
                    ), this
                )
            }
        }
    }

    @Test
    fun `Authorized - Successfully update member name`() = testApp { client ->
        val registeredUser = ServiceFactory.authService.register(UserDTO( "email", "password"))

        ServiceFactory.memberService.addMember(Member(registeredUser.id!!, MEMBER_NAME))
        client.put("/member/update-member-name") {
            val token = JwtConfig.createToken(registeredUser.id!!)
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(MemberDTO("new_name"))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            body<Member>().apply {
                assertEquals("new_name", name)
                assertTrue(uid.isNotBlank())
            }
        }
    }

    @Test
    fun `Authorized - Fail to update member name when member not exist`() = testApp { client ->
        client.put("/member/update-member-name") {
            val token = JwtConfig.createToken(firstUserId)
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(MemberDTO("new_name"))
        }.apply {
            assertEquals(HttpStatusCode.NotFound, status)
            body<ErrorResponse>().apply {
                assertEquals(
                    ErrorResponse(
                        ExplicitException.MemberNotFound.description,
                        ExplicitException.MemberNotFound.message
                    ), this
                )
            }
        }
    }

    private companion object {
        val firstUserId = UUID.randomUUID().toString()
        const val MEMBER_NAME = "member_name"
    }
}