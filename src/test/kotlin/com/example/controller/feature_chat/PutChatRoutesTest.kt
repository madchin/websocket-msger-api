package com.example.controller.feature_chat

import com.example.controller.test_util.testApp
import com.example.controller.util.JwtConfig
import com.example.controller.util.ValidationReason
import com.example.model.Chat
import com.example.model.ChatDTO
import com.example.service.ServiceFactory
import com.example.util.EntityFieldLength
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class PutChatRoutesTest {
    @Test
    fun `Authorized - Fail to update chat name for chat which user is not member`() = testApp { client ->
        val createdChat = ServiceFactory.chatService.createChat(ChatDTO(chatToCreateName), FIRST_USER_ID)
        client.put("/chat/${createdChat.id}/change-name") {
            val token = JwtConfig.createToken(SECOND_USER_ID)
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(ChatDTO(chatToUpdateName))
        }.apply {
            assertEquals(HttpStatusCode.Forbidden, status)
        }
    }

    @Test
    fun `Authorized - Successfully update chat name for chat when user is chat member`() = testApp { client ->
        val createdChat = ServiceFactory.chatService.createChat(ChatDTO(chatToCreateName), FIRST_USER_ID)
        ServiceFactory.chatService.joinChat(createdChat.id!!, SECOND_USER_ID)
        client.put("/chat/${createdChat.id}/change-name") {
            val token = JwtConfig.createToken(SECOND_USER_ID)
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(ChatDTO(chatToUpdateName))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            body<Chat>().apply {
                assertEquals(chatToUpdateName, name)
            }
        }
    }

    @Test
    fun `Authorized - Successfully update chat name`() = testApp { client ->
        val createdChat = ServiceFactory.chatService.createChat(ChatDTO(chatToCreateName), FIRST_USER_ID)
        client.put("/chat/${createdChat.id}/change-name") {
            val token = JwtConfig.createToken(FIRST_USER_ID)
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(ChatDTO(chatToUpdateName))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            body<Chat>().apply {
                assertEquals(chatToUpdateName, name)
            }
        }
    }

    @Test
    fun `Unauthorized - Fail to update chat name`() = testApp { client ->
        val createdChat = ServiceFactory.chatService.createChat(ChatDTO(chatToCreateName), FIRST_USER_ID)
        client.put("/chat/${createdChat.id}/change-name").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `Authorized - Fail to update chat name for chat which not exists`() = testApp { client ->
        client.put("/chat/$randomUUID/change-name") {
            val token = JwtConfig.createToken(FIRST_USER_ID)
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(ChatDTO(chatToUpdateName))
        }.apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun `Authorized - Fail to update chat name for chat which name is blank`() = testApp { client ->
        client.put("/chat/$randomUUID/change-name") {
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

    @Test
    fun `Authorized - Fail to update chat name for chat which name violates min chat name length`() =
        testApp { client ->
            client.put("/chat/$randomUUID/change-name") {
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
    fun `Authorized - Fail to update chat name for chat which name violates max chat name length`() =
        testApp { client ->
            client.put("/chat/$randomUUID/change-name") {
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

    private companion object {
        const val FIRST_USER_ID = "first_user_id"
        const val SECOND_USER_ID = "second_user_id"
        const val CHAT_NAME_BLANK = "  "
        val chatToCreateName = "c".repeat(EntityFieldLength.Chats.Name.minLength + 1)
        val chatToUpdateName = "b".repeat(EntityFieldLength.Chats.Name.minLength + 1)
        val chatNameMinLengthViolation = "c".repeat(EntityFieldLength.Chats.Name.minLength - 1)
        val chatNameMaxLengthViolation = "c".repeat(EntityFieldLength.Chats.Name.maxLength + 1)
        val randomUUID = UUID.randomUUID().toString()
    }
}