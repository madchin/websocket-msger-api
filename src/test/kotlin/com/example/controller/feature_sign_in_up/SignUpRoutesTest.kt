package com.example.controller.feature_sign_in_up

import com.example.model.UserDTO
import com.example.service.ServiceFactory
import com.example.util.ErrorResponse
import com.example.util.ExplicitException
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class SignUpRoutesTest {
    @Test
    fun `Fail to sign up when user with same username already exist`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        startApplication()
        val user = UserDTO(USERNAME, EMAIL, PASSWORD)
        ServiceFactory.authService.register(user)
        client.post("/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<ErrorResponse>().apply {
                assertEquals(ExplicitException.DuplicateUser.message, message)
            }
        }
    }

    @Test
    fun `Successfully sign up`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        client.post("/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(UserDTO(USERNAME, EMAIL, PASSWORD))
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
        }
    }

    private companion object {
        const val USERNAME = "username"
        const val PASSWORD = "password"
        const val EMAIL = "email"
    }
}

//FIXME add tests for requestValidationException handler in all tests in com.example.controller.*