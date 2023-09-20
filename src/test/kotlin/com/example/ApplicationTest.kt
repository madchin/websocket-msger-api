package com.example

import com.example.data.Services
import com.example.data.service.ChatService
import com.example.data.service.MemberService
import com.example.data.service.MessageService
import com.example.data.service.UserService
import com.example.plugins.configureRouting
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class FakeServices : Services {
    override val chatService: ChatService
        get() = TODO("Not yet implemented")
    override val memberService: MemberService
        get() = TODO("Not yet implemented")
    override val messageService: MessageService
        get() = TODO("Not yet implemented")
    override val userService: UserService
        get() = TODO("Not yet implemented")
}

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting(FakeServices())
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}
