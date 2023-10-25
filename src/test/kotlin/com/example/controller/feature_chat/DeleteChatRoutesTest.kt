package com.example.controller.feature_chat

import com.example.controller.util.JwtConfig
import com.example.model.ChatDTO
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

class DeleteChatRoutesTest {
    @Test
    fun `Unauthorized - Fail to delete chat`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        client.delete("/chat/$randomUUID").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `Authorized - Fail to delete chat which user is not member`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        startApplication()
        val createdChat = ServiceFactory.chatService.createChat(ChatDTO(CHAT_TO_CREATE_NAME), FIRST_USER_ID)
        client.delete("/chat/${createdChat.id}") {
            val token = JwtConfig.createToken(SECOND_USER_ID)
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(CHAT_TO_CREATE_NAME)
        }.apply {
            assertEquals(HttpStatusCode.Forbidden, status)
        }
    }

    @Test
    fun `Authorized - Successfully delete chat which user is member`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        startApplication()
        val createdChat = ServiceFactory.chatService.createChat(ChatDTO(CHAT_TO_CREATE_NAME), SECOND_USER_ID)
        client.delete("/chat/${createdChat.id}") {
            val token = JwtConfig.createToken(SECOND_USER_ID)
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(CHAT_TO_CREATE_NAME)
        }.apply {
            assertEquals(HttpStatusCode.NoContent, status)
        }
    }

    private companion object {
        const val FIRST_USER_ID = "first_user_id"
        const val SECOND_USER_ID = "second_user_id"
        const val CHAT_TO_CREATE_NAME = "chatName"
        val randomUUID = UUID.randomUUID().toString()
    }
}