package com.example.controller.feature_member_manage

import com.example.controller.util.JwtConfig
import com.example.model.Member
import com.example.model.MemberDTO
import com.example.model.UserDTO
import com.example.service.ServiceFactory
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class DeleteMemberRoutesTest {
    @Test
    fun `Unauthorized - fail to delete member`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        client.delete("/member").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `Authorized - Successfully delete member`() = testApplication {
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
        client.delete("/member") {
            val token = JwtConfig.createToken(registeredUser.id!!)
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(MemberDTO(MEMBER_NAME))
        }.apply {
            assertEquals(HttpStatusCode.NoContent, status)
        }
    }

    @Test
    fun `Authorized - Fail to delete member when member not exist`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        startApplication()
        client.delete("/member") {
            val token = JwtConfig.createToken(firstUserId)
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(MemberDTO(MEMBER_NAME))
        }.apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    private companion object {
        val firstUserId = UUID.randomUUID().toString()
        const val MEMBER_NAME = "member_name"
    }
}