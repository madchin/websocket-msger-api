package com.example.controller.feature_member_manage

import com.example.controller.util.JwtConfig
import com.example.dao.RepositoryFactory
import com.example.model.Member
import com.example.model.User
import com.example.model.UserDTO
import com.example.service.ServiceFactory
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class GetMemberRoutesTest {
    @Test
    fun `Unauthorized - fail to get member`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        client.get("/member").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `Authorized - Fail to get member when there is no member with current logged in user id`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        startApplication()
        client.get("/member") {
            val token = JwtConfig.createToken("user_id")
            bearerAuth(token)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
        }
    }

    @Test
    fun `Authorized - Successfully get member when member exists`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        startApplication()
        val registeredUser = ServiceFactory.authService.register(UserDTO("username","email","password"))
        val token = JwtConfig.createToken(registeredUser.id!!)
        ServiceFactory.memberService.addMember(Member(registeredUser.id!!, "memberName"))
        client.get("/member") {
            bearerAuth(token)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }
}