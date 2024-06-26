package com.example.controller.feature_chat

import com.example.TestConfig
import com.example.controller.test_util.testApp
import com.example.controller.util.ErrorResponse
import com.example.controller.util.JwtConfig
import com.example.model.Chat
import com.example.model.ChatDTO
import com.example.service.ServiceFactory
import com.example.util.ExplicitException
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class GetChatRoutesTest : TestConfig() {
    @Test
    fun `Unauthorized - fail to get chat`() = testApp(false) { client ->
        client.get("/chat/$randomUUID").apply {
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
    fun `Authorized - Fail to get chat which not exists`() = testApp { client ->
        client.get("/chat/$randomUUID") {
            val token = JwtConfig.createToken(FIRST_USER_ID)
            bearerAuth(token)
        }.apply {
            assertEquals(HttpStatusCode.NotFound, status)
            body<ErrorResponse>().apply {
                assertEquals(
                    ErrorResponse(
                        ExplicitException.ChatNotFound.description,
                        ExplicitException.ChatNotFound.message
                    ), this
                )
            }
        }
    }

    @Test
    fun `Authorized - Successfully get chat which user is member`() = testApp { client ->
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
    fun `Authorized - Fail to get chat which user is not member`() = testApp { client ->
        val createdChat = ServiceFactory.chatService.createChat(ChatDTO("example"), FIRST_USER_ID)

        client.get("/chat/${createdChat.id}") {
            val token = JwtConfig.createToken(SECOND_USER_ID)
            bearerAuth(token)
        }.apply {
            assertEquals(HttpStatusCode.Forbidden, status)
            body<ErrorResponse>().apply {
                assertEquals(
                    ErrorResponse(
                        ExplicitException.Forbidden.description,
                        ExplicitException.Forbidden.message
                    ), this
                )
            }
        }
    }

    private companion object {
        const val FIRST_USER_ID = "first_user_id"
        const val SECOND_USER_ID = "second_user_id"
        val randomUUID = UUID.randomUUID().toString()
    }
}