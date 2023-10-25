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
import kotlin.test.assertTrue

typealias SignInResponse = HashMap<String, String>


class SignInRoutesTest {
    @Test
    fun `Fail to sign in when user not exist`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val (username, email, password) = generateCredentials()
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(UserDTO(username, email, password))
        }.apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun `Fail to sign in when wrong password provided`() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val (username, email, password) = generateCredentials()
        startApplication()
        ServiceFactory.authService.register(UserDTO(username, email, password + "random"))
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(UserDTO(username, email, password))
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<ErrorResponse>().apply {
                assertEquals(ExplicitException.WrongCredentials.message, message)
            }
        }
    }

    @Test
    fun `Successfully sign in`() = testApplication {
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
        val createdUser = ServiceFactory.authService.register(user)
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            body<SignInResponse>().apply {
                assertTrue { this["token"] != null && this["token"]!!.isNotBlank() }
                assertEquals(this["uid"], createdUser.id!!)
            }
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