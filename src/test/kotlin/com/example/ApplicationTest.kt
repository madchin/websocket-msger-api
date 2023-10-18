package com.example

import com.example.domain.service.Services
import com.example.domain.service.ChatService
import com.example.domain.service.MemberService
import com.example.domain.service.MessageService
import com.example.domain.service.UserService
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
