package com.example.controller.feature_chat

import com.example.TestConfig
import com.example.controller.test_util.testAppSocket
import com.example.controller.util.JwtConfig
import com.example.model.ChatDTO
import com.example.model.Member
import com.example.model.MessageDTO
import com.example.model.UserDTO
import com.example.service.ServiceFactory
import com.example.socket.ChatRoomSocketHandlerImpl
import com.example.util.EntityFieldLength
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class SocketChatTest : TestConfig() {

    @Test
    fun `Authorized - close websocket session when chat not found`() = testAppSocket { client ->
        val randomUUID = UUID.randomUUID().toString()
        client.webSocket("/chat/socket/$randomUUID", {
            val token = JwtConfig.createToken(firstUserId)
            bearerAuth(token)
        }) {
            assertEquals(closeReason.await()?.message, ChatRoomSocketHandlerImpl.CHAT_NOT_FOUND_MESSAGE)
        }
    }

    @Test
    fun `Authorized - close websocket session when member not found`() = testAppSocket { client ->
        val createdChat = ServiceFactory.chatService.createChat(ChatDTO(chatNameToCreate), firstUserId)

        client.webSocket("/chat/socket/${createdChat.id}", {
            val token = JwtConfig.createToken(firstUserId)
            bearerAuth(token)
        }) {
            assertEquals(closeReason.await()?.message, ChatRoomSocketHandlerImpl.MEMBER_NOT_FOUND_MESSAGE)
        }
    }

    @Test
    fun `Authorized - Successfully join to socket chat`() = testAppSocket { client ->
        val createdChat = ServiceFactory.chatService.createChat(ChatDTO(chatNameToCreate), firstUserId)

        val createdUser =
            ServiceFactory.authService.register(UserDTO(generateEmail(), generatePassword()))

        val member = ServiceFactory.memberService.addMember(Member(createdUser.id!!, FIRST_MEMBER_NAME))

        client.webSocket("/chat/socket/${createdChat.id}", {
            val token = JwtConfig.createToken(member.uid)
            bearerAuth(token)
        }) {
            val frame = (incoming.receive() as? Frame.Text)?.readText() ?: ""
            val decodedMessage: MessageDTO = Json.decodeFromString(frame)
            assertEquals(decodedMessage.sender, member.uid)
            assertEquals(decodedMessage.content, "Member ${member.name} joined chat")
        }
    }

//FIXME
//    @Test
//    fun `Unauthorized - fail to join chat`() = testAppSocket { client ->
//        val randomUUID = UUID.randomUUID().toString()
//        client.webSocket("/chat/socket/$randomUUID") {
//            assertEquals(closeReason.await()?.message, "Unauthorized")
//        }
//    }

    private companion object {
        val firstUserId = UUID.randomUUID().toString()
        const val FIRST_MEMBER_NAME = "first_name"
        val chatNameToCreate = "c".repeat(EntityFieldLength.Chats.Name.minLength + 1)
        fun generatePassword() = "p".repeat(EntityFieldLength.Users.Password.minLength + 1)
        fun generateEmail() = "e".repeat(EntityFieldLength.Users.Email.minLength + 1)
    }
}
















