package com.example.controller.feature_member_manage

import com.example.controller.util.JwtConfig
import com.example.model.Member
import com.example.model.MemberDTO
import com.example.model.UserDTO
import com.example.service.ServiceFactory
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PutMemberRoutesTest {
    @Test
    fun `Unauthorized - fail to update member name`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }

        client.put("/member/update-member-name").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `Authorized - Successfully update member name`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        startApplication()
        val registeredUser = ServiceFactory.authService.register(UserDTO("username", "email", "password"))
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
    fun `Authorized - Fail to update member name when member not exist`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        startApplication()
        client.put("/member/update-member-name") {
            val token = JwtConfig.createToken(firstUserId)
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(MemberDTO("new_name"))
        }.apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    private companion object {
        val firstUserId = UUID.randomUUID().toString()
        const val MEMBER_NAME = "member_name"
    }
}