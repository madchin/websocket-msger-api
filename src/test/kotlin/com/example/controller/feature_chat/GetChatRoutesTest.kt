package com.example.controller.feature_chat

import com.example.controller.util.JwtConfig
import com.example.model.Chat
import com.example.model.ChatDTO
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

class GetChatRoutesTest {
    @Test
    fun `Unauthorized - fail to get chat`() = testApplication {
        val randomUid = UUID.randomUUID().toString()
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        client.get("/chat/$randomUid").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `Authorized - Fail to get chat which not exists`() = testApplication {
        val randomUid = UUID.randomUUID().toString()
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        startApplication()
        client.get("/chat/$randomUid") {
            val token = JwtConfig.createToken(FIRST_USER_ID)
            bearerAuth(token)
        }.apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun `Authorized - Successfully get chat which user is member`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        startApplication()
        val createdChat = ServiceFactory.chatService.createChat(ChatDTO("example"), FIRST_USER_ID)
        client.get("/chat/${createdChat.id}") {
            val token = JwtConfig.createToken(FIRST_USER_ID)
            bearerAuth(token)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            body<Chat>().apply {
                assertEquals("example", name)
            }
        }
    }

    @Test
    fun `Authorized - Fail to get chat which user is not member`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        startApplication()
        val createdChat = ServiceFactory.chatService.createChat(ChatDTO("example"), FIRST_USER_ID)
        client.get("/chat/${createdChat.id}") {
            val token = JwtConfig.createToken(SECOND_USER_ID)
            bearerAuth(token)
        }.apply {
            assertEquals(HttpStatusCode.Forbidden, status)
        }
    }

    private companion object {
        const val FIRST_USER_ID = "first_user_id"
        const val SECOND_USER_ID = "second_user_id"
    }
}