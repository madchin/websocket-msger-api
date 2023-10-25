package com.example.controller.feature_chat

import com.example.controller.util.JwtConfig
import com.example.controller.util.ValidationReason
import com.example.model.Chat
import com.example.model.ChatDTO
import com.example.service.ServiceFactory
import com.example.util.EntityFieldLength
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
        client.post("/chat").apply {
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
            setBody(ChatDTO(chatToCreateName))
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
            body<Chat>().apply {
                val addedMember = this.lastSeenMembers.find { member -> member.containsKey(FIRST_USER_ID) }
                val addedMemberTimestamp = addedMember?.get(FIRST_USER_ID)
                assertNotNull(addedMember)
                assertTrue(addedMemberTimestamp is Long)
                assertEquals(chatToCreateName, name)
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
        val chatToCreate = ChatDTO(chatToCreateName)
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
                assertEquals(chatToCreateName, name)
            }
        }
    }

    @Test
    fun `Unauthorized - Fail to join chat`() = testApplication {
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
        val createdChat = ServiceFactory.chatService.createChat(ChatDTO(chatToCreateName), FIRST_USER_ID)
        client.post("/chat/${createdChat.id}/join-chat") {
            val token = JwtConfig.createToken(SECOND_USER_ID)
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(ChatDTO(chatToCreateName))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            body<Chat>().apply {
                val addedMember = this.lastSeenMembers.find { member -> member.containsKey(SECOND_USER_ID) }
                val addedMemberTimestamp = addedMember?.get(SECOND_USER_ID)
                assertEquals(chatToCreateName, name)
                assertNotNull(addedMember)
                assertTrue(addedMemberTimestamp is Long)
            }
        }
    }

    @Test
    fun `Authorized - Fail to create chat which chat name violates min chat name length`() = testApplication {
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
            setBody(ChatDTO(chatNameMinLengthViolation))
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<String>().apply {
                assertEquals(
                    this, ValidationReason.length(
                        ChatDTO::name.name,
                        chatNameMinLengthViolation.length,
                        EntityFieldLength.Chats.Name.minLength,
                        EntityFieldLength.Chats.Name.maxLength
                    )
                )
            }
        }
    }

    @Test
    fun `Authorized - Fail to create chat which chat name violates max chat name length`() = testApplication {
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
            setBody(ChatDTO(chatNameMaxLengthViolation))
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<String>().apply {
                assertEquals(
                    this, ValidationReason.length(
                        ChatDTO::name.name,
                        chatNameMaxLengthViolation.length,
                        EntityFieldLength.Chats.Name.minLength,
                        EntityFieldLength.Chats.Name.maxLength
                    )
                )
            }
        }
    }

    @Test
    fun `Authorized - Fail to create chat which chat name is blank`() = testApplication {
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
            setBody(ChatDTO(CHAT_NAME_BLANK))
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<String>().apply {
                assertEquals(this, ValidationReason.blank(ChatDTO::name.name))
            }
        }
    }

    private companion object {
        const val FIRST_USER_ID = "first_user_id"
        const val SECOND_USER_ID = "second_user_id"
        const val CHAT_NAME_BLANK = "  "
        val chatToCreateName = "c".repeat(EntityFieldLength.Chats.Name.minLength + 1)
        val chatNameMinLengthViolation = "c".repeat(EntityFieldLength.Chats.Name.minLength - 1)
        val chatNameMaxLengthViolation = "c".repeat(EntityFieldLength.Chats.Name.maxLength + 1)
        val randomUUID = UUID.randomUUID().toString()
    }
}