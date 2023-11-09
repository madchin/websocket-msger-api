package com.example.controller.feature_chat

import com.example.TestConfig
import com.example.controller.test_util.testApp
import com.example.controller.util.JwtConfig
import com.example.model.ChatDTO
import com.example.service.ServiceFactory
import io.ktor.client.request.*
import io.ktor.http.*
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class DeleteChatRoutesTest : TestConfig() {
    @Test
    fun `Unauthorized - Fail to delete chat`() = testApp(false) { client ->
        client.delete("/chat/$randomUUID").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `Authorized - Fail to delete chat which user is not member`() = testApp { client ->
        val createdChat = ServiceFactory.chatService.createChat(ChatDTO(CHAT_TO_DELETE_NAME), FIRST_USER_ID)

        client.delete("/chat/${createdChat.id}") {
            val token = JwtConfig.createToken(SECOND_USER_ID)
            bearerAuth(token)
        }.apply {
            assertEquals(HttpStatusCode.Forbidden, status)
        }
    }

    @Test
    fun `Authorized - Successfully delete chat which user is member`() = testApp { client ->
        val createdChat = ServiceFactory.chatService.createChat(ChatDTO(CHAT_TO_DELETE_NAME), SECOND_USER_ID)

        client.delete("/chat/${createdChat.id}") {
            val token = JwtConfig.createToken(SECOND_USER_ID)
            bearerAuth(token)
        }.apply {
            assertEquals(HttpStatusCode.NoContent, status)
        }
    }


    private companion object {
        const val FIRST_USER_ID = "first_user_id"
        const val SECOND_USER_ID = "second_user_id"
        const val CHAT_TO_DELETE_NAME = "chatName"
        val randomUUID = UUID.randomUUID().toString()
    }
}