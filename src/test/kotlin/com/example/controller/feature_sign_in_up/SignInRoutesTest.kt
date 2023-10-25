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
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(UserDTO(USERNAME, EMAIL, PASSWORD))
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
        startApplication()
        ServiceFactory.authService.register(UserDTO(USERNAME, EMAIL, PASSWORD + "random"))
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(UserDTO(USERNAME, EMAIL, PASSWORD))
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
        val createdUser = ServiceFactory.authService.register(UserDTO(USERNAME, EMAIL, PASSWORD))
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(UserDTO(USERNAME, EMAIL, PASSWORD))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            body<SignInResponse>().apply {
                assertTrue { this["token"] != null && this["token"]!!.isNotBlank() }
                assertEquals(this["uid"], createdUser.id!!)
            }
        }
    }

    private companion object {
        const val USERNAME = "username"
        const val PASSWORD = "password"
        const val EMAIL = "email"
    }
}