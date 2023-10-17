package com.example.controller.chats_basic

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.junit.Assert.assertEquals
import org.junit.Test

class ChatRoutesTest {
    @Test
    fun test_get_chat_unauthorized_fail() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }

        val response = client.get("/chats/xd")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

}