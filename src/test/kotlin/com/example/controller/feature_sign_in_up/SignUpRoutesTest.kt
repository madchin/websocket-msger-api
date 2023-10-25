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
        val (username, email, password) = generateCredentials()
        val user = UserDTO(username, email, password)

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
        val (username, email, password) = generateCredentials()
        client.post("/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(UserDTO(username, email, password))
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
        }
    }

    private companion object {
        var usernameCounter = 1
            get() = field++
            private set
        var passwordCounter = 1
            get() = field++
            private set
        var emailCounter: Int = 1
            get() = field++
            private set

        fun generateUsername() = "username$usernameCounter"
        fun generatePassword() = "password$passwordCounter"
        fun generateEmail() = "email$emailCounter"
        fun generateCredentials() = Triple(generateUsername(), generatePassword(), generateEmail())
    }
}

//FIXME add tests for requestValidationException handler in all tests in com.example.controller.*