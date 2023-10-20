package com.example

import com.example.model.*
import com.example.config.plugin.configureRouting
import com.example.service.AuthService
import com.example.service.ChatService
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class FakeChatService : ChatService {
    override suspend fun createChat(chat: ChatDTO, userId: String): Chat {
        TODO("Not yet implemented")
    }

    override suspend fun getChat(chatId: String, userId: String): Chat {
        TODO("Not yet implemented")
    }

    override suspend fun changeChatName(chatId: String, name: String, userId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteChat(chatId: String, userId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun joinChat(chatId: String, userId: String): Chat {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(message: Message): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun readMessages(chatId: String): List<Message> {
        TODO("Not yet implemented")
    }
}

class FakeAuthService : AuthService {
    override suspend fun login(userDto: UserDTO): User {
        TODO("Not yet implemented")
    }

    override suspend fun register(userDto: UserDTO) {
        TODO("Not yet implemented")
    }
}

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting(FakeChatService(), FakeAuthService())
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}
