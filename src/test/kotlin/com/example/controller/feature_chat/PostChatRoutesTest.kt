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
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PostChatRoutesTest {
    @Test
    fun `Unauthorized - Fail to create chat`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        client.post("/chat") {
            contentType(ContentType.Application.Json)
            setBody(ChatDTO(CHAT_TO_CREATE_NAME))
        }.apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `Authorized - Successfully create chat`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        startApplication()
        client.post("/chat") {
            val token = JwtConfig.createToken(FIRST_USER_ID)
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(ChatDTO(CHAT_TO_CREATE_NAME))
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
            body<Chat>().apply {
                val addedMember = this.lastSeenMembers.find { member -> member.containsKey(FIRST_USER_ID) }
                val addedMemberTimestamp = addedMember?.get(FIRST_USER_ID)
                assertNotNull(addedMember)
                assertTrue(addedMemberTimestamp is Long)
                assertEquals(CHAT_TO_CREATE_NAME, name)
            }
        }
    }

    @Test
    fun `Authorized - Successfully create chat with name which already exists in database`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        startApplication()
        val chatToCreate = ChatDTO(CHAT_TO_CREATE_NAME)
        val token = JwtConfig.createToken(FIRST_USER_ID)
        ServiceFactory.chatService.createChat(chatToCreate, FIRST_USER_ID)
        client.post("/chat") {
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(chatToCreate)
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
            body<Chat>().apply {
                val addedMember = this.lastSeenMembers.find { member -> member.containsKey(FIRST_USER_ID) }
                val addedMemberTimestamp = addedMember?.get(FIRST_USER_ID)
                assertNotNull(addedMember)
                assertTrue(addedMemberTimestamp is Long)
                assertEquals(CHAT_TO_CREATE_NAME, name)
            }
        }
    }

    @Test
    fun `Unauthorized - Fail to join chat`() = testApplication {
        val randomUUID = UUID.randomUUID().toString()
        environment {
            config = ApplicationConfig("application-test.conf")
        }

        startApplication()
        client.post("/chat/$randomUUID/join-chat").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `Authorized - Fail to join chat which not exists`() = testApplication {
        val randomUUID = UUID.randomUUID().toString()
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        startApplication()
        client.post("/chat/$randomUUID/join-chat") {
            val token = JwtConfig.createToken(FIRST_USER_ID)
            bearerAuth(token)
        }.apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun `Authorized - Successfully join chat`() = testApplication {
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
        client.post("/chat/${createdChat.id}/join-chat") {
            val token = JwtConfig.createToken(SECOND_USER_ID)
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(ChatDTO(CHAT_TO_CREATE_NAME))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            body<Chat>().apply {
                val addedMember = this.lastSeenMembers.find { member -> member.containsKey(SECOND_USER_ID) }
                val addedMemberTimestamp = addedMember?.get(SECOND_USER_ID)
                assertEquals(CHAT_TO_CREATE_NAME, name)
                assertNotNull(addedMember)
                assertTrue(addedMemberTimestamp is Long)
            }
        }
    }

    private companion object {
        const val FIRST_USER_ID = "first_user_id"
        const val SECOND_USER_ID = "second_user_id"
        const val CHAT_TO_CREATE_NAME = "chatName"
    }
}