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

class PostMemberRoutesTest : TestConfig() {

    @Test
    fun `Unauthorized - fail to add member`() = testApp(false) { client ->
        client.post("/member/add-member").apply {
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
    fun `Authorized - Successfully add member`() = testApp { client ->
        val registeredUser = ServiceFactory.authService.register(UserDTO("username", "email", "password"))

        client.post("/member/add-member") {
            val token = JwtConfig.createToken(registeredUser.id!!)
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(MemberDTO(MEMBER_NAME))
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
            body<Member>().apply {
                assertEquals(MEMBER_NAME, name)
                assertTrue(uid.isNotBlank())
            }
        }
    }

    @Test
    fun `Authorized - Fail to add member when user not exist`() = testApp { client ->
        client.post("/member/add-member") {
            val token = JwtConfig.createToken(firstUserId)
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(MemberDTO(MEMBER_NAME))
        }.apply {
            assertEquals(HttpStatusCode.NotFound, status)
            body<ErrorResponse>().apply {
                assertEquals(
                    ErrorResponse(
                        ExplicitException.UserNotFound.description,
                        ExplicitException.UserNotFound.message
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