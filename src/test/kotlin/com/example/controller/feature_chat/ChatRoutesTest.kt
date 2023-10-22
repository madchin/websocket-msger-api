package com.example.controller.feature_chat

import com.example.model.ChatDTO
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlin.test.*

class ChatRoutesTest {
    @Test
    fun `get chat which not exist`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }

        val response = client.get("/chats/xd")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `fail to get chat when unauthorized`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/chat") {
            contentType(ContentType.Application.Json)
            setBody(ChatDTO("example"))
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }
}