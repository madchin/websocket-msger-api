package com.example.controller.feature_chat

import com.example.controller.feature_sign_in_up.SignInRoutesTest
import com.example.controller.test_util.testAppSocket
import com.example.controller.util.JwtConfig
import com.example.model.ChatDTO
import com.example.model.Member
import com.example.model.UserDTO
import com.example.service.ServiceFactory
import com.example.socket.ChatRoomSocketHandlerImpl
import com.example.socket.SocketFactory
import com.example.util.EntityFieldLength
import com.example.util.ErrorResponse
import com.example.util.ExplicitException
import io.ktor.client.call.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SocketChatTest {

    @Test
    fun `Authorized - close websocket session when chat not found`() = testAppSocket {client ->
        val randomUUID = UUID.randomUUID().toString()
        client.webSocket("/chat/socket/$randomUUID", {
            val token = JwtConfig.createToken(firstUserId)
            bearerAuth(token)
        }) {
            assertEquals(ChatRoomSocketHandlerImpl.CHAT_NOT_FOUND_MESSAGE, closeReason.await()?.message)
        }
    }

    @Test
    fun `Authorized - close websocket session when member not found`() = testAppSocket {client ->
        val createdChat = ServiceFactory.chatService.createChat(ChatDTO(chatNameToCreate), firstUserId)
        client.webSocket("/chat/socket/${createdChat.id}", {
            val token = JwtConfig.createToken(firstUserId)
            bearerAuth(token)
        }) {
            assertEquals(ChatRoomSocketHandlerImpl.MEMBER_NOT_FOUND_MESSAGE, closeReason.await()?.message)
        }
    }

    @Test
    fun `Authorized - Successfully joined socket chat`() = testAppSocket {client ->
        val createdChat = ServiceFactory.chatService.createChat(ChatDTO(chatNameToCreate), firstUserId)
        val createdUser = ServiceFactory.authService.register(UserDTO(generateUsername(), generateEmail(), generatePassword()))
        val member = ServiceFactory.memberService.addMember(Member(createdUser.id!!,FIRST_MEMBER_NAME))

        client.webSocket("/chat/socket/${createdChat.id}", {
            val token = JwtConfig.createToken(createdUser.id!!)
            bearerAuth(token)
        }) {
            val chatMembers = SocketFactory.chatRoomHandler.chatMembers
            delay(2000L)
            assertTrue { chatMembers.firstOrNull { it.session == this } != null }
            assertTrue { chatMembers.firstOrNull { it.member == member } != null }
        }
    }

    private companion object {
        val firstUserId = UUID.randomUUID().toString()
        const val FIRST_MEMBER_NAME = "first_name"
        val chatNameToCreate = "c".repeat(EntityFieldLength.Chats.Name.minLength + 1)
        fun generateUsername() = "u".repeat(EntityFieldLength.Users.Username.minLength + 1)
        fun generatePassword() = "p".repeat(EntityFieldLength.Users.Password.minLength + 1)
        fun generateEmail() = "e".repeat(EntityFieldLength.Users.Email.minLength + 1)
    }
}
















