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

class PutChatRoutesTest {
    @Test
    fun `Authorized - Fail to update chat name for chat which we are not member`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        startApplication()
        val createdChat = ServiceFactory.chatService.createChat(ChatDTO(CHAT_NAME), FIRST_USER_ID)
        client.put("/chat/${createdChat.id}/change-name") {
            val token = JwtConfig.createToken(SECOND_USER_ID)
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(ChatDTO("newName"))
        }.apply {
            assertEquals(HttpStatusCode.Forbidden, status)
        }
    }

    @Test
    fun `Authorized - Successfully update chat name for chat when user is chat member`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        startApplication()
        val createdChat = ServiceFactory.chatService.createChat(ChatDTO(CHAT_NAME), FIRST_USER_ID)
        ServiceFactory.chatService.joinChat(createdChat.id!!, SECOND_USER_ID)
        client.put("/chat/${createdChat.id}/change-name") {
            val token = JwtConfig.createToken(SECOND_USER_ID)
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(ChatDTO("newName"))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            body<Chat>().apply {
                assertEquals("newName", name)
            }
        }
    }

    @Test
    fun `Authorized - Successfully update chat name`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        startApplication()
        val createdChat = ServiceFactory.chatService.createChat(ChatDTO(CHAT_NAME), FIRST_USER_ID)
        client.put("/chat/${createdChat.id}/change-name") {
            val token = JwtConfig.createToken(FIRST_USER_ID)
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(ChatDTO("newName"))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            body<Chat>().apply {
                assertEquals("newName", name)
            }
        }
    }

    @Test
    fun `Unauthorized - Fail to update chat name`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        startApplication()
        val createdChat = ServiceFactory.chatService.createChat(ChatDTO(CHAT_NAME), FIRST_USER_ID)
        client.put("/chat/${createdChat.id}/change-name") {
            contentType(ContentType.Application.Json)
            setBody(ChatDTO("newName"))
        }.apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `Authorized - Fail to update chat name for chat which not exists`() = testApplication {
        val randomUUID = UUID.randomUUID().toString()
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        startApplication()
        client.put("/chat/$randomUUID/change-name") {
            val token = JwtConfig.createToken(FIRST_USER_ID)
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(ChatDTO("newName"))
        }.apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    private companion object {
        const val FIRST_USER_ID = "first_user_id"
        const val SECOND_USER_ID = "second_user_id"
        const val CHAT_NAME = "chatName"
    }
}