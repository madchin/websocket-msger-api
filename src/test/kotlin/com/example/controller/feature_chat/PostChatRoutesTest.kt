package com.example.controller.feature_chat

import com.example.controller.util.JwtConfig
import com.example.model.Chat
import com.example.model.ChatDTO
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PostChatRoutesTest {
    @Test
    fun `Fail to post chat when unauthorized`() = testApplication {
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
            setBody(ChatDTO("example"))
        }.apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `Successfully post chat when authorized`() = testApplication {
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
            val token = JwtConfig.createToken("user_id")
            bearerAuth(token)
            contentType(ContentType.Application.Json)
            setBody(ChatDTO("example"))
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
            body<Chat>().apply {
                val addedMember = this.lastSeenMembers.find { member -> member.containsKey("user_id") }
                val addedMemberTimestamp = addedMember?.get("user_id")
                assertNotNull(addedMember)
                assertTrue(addedMemberTimestamp is Long)
                assertEquals("example", name)
            }
        }
    }
}