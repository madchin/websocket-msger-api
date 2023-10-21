package com.example.controller.feature_chat

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.junit.Assert.assertEquals
import org.junit.Test

class ChatRoutesTest {
    @Test
    fun `get chat which not exist`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }

        val response = client.get("/chats/xd")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

}