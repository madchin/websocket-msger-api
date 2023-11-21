package com.example.controller.feature_sign_in_up

import com.example.TestConfig
import com.example.controller.test_util.testApp
import com.example.controller.util.ErrorResponse
import com.example.controller.util.ValidationReason
import com.example.model.UserDTO
import com.example.service.ServiceFactory
import com.example.util.EntityFieldLength
import com.example.util.ExplicitException
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

typealias SignInResponse = HashMap<String, String>


class SignInRoutesTest : TestConfig() {
    @Test
    fun `Fail to sign in when user not exist`() = testApp(false) { client ->
        val (email, password) = generateCredentials()
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(UserDTO(email, password))
        }.apply {
            assertEquals(HttpStatusCode.NotFound, status)
            body<ErrorResponse>().apply {
                assertEquals(
                    ErrorResponse(
                        ExplicitException.UserNotFound.description,
                        ExplicitException.UserNotFound.message
                    ), this
                )
            }
        }
    }

    @Test
    fun `Fail to sign in when wrong password provided`() = testApp { client ->
        val (email, password) = generateCredentials()
        ServiceFactory.authService.register(UserDTO(email, password + "random"))
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(UserDTO(email, password))
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<ErrorResponse>().apply {
                assertEquals(
                    ErrorResponse(
                        ExplicitException.WrongCredentials.description,
                        ExplicitException.WrongCredentials.message
                    ), this
                )
            }
        }
    }

    @Test
    fun `Successfully sign in`() = testApp { client ->
        val (email, password) = generateCredentials()
        val user = UserDTO(email, password)
        val createdUser = ServiceFactory.authService.register(user)
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            body<SignInResponse>().apply {
                assertTrue { this["token"] != null && this["token"]!!.isNotBlank() }
                assertEquals(createdUser.id!!, this["uid"])
            }
        }
    }

    @Test
    fun `Fail to sign in when password is blank`() = testApp(false) { client ->
        val (email, _) = generateCredentials()
        val user = UserDTO(email, PASSWORD_BLANK)
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<ErrorResponse>().apply {
                assertEquals(
                    ErrorResponse(
                        ErrorResponse.Type.VALIDATION,
                        ValidationReason.blank(UserDTO::password.name)
                    ), this
                )
            }
        }
    }

    @Test
    fun `Fail to sign in when email is blank`() = testApp(false) { client ->
        val (_, password) = generateCredentials()
        val user = UserDTO(EMAIL_BLANK, password)
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<ErrorResponse>().apply {
                assertEquals(
                    ErrorResponse(
                        ErrorResponse.Type.VALIDATION,
                        ValidationReason.blank(UserDTO::email.name)
                    ), this
                )
            }
        }
    }

    @Test
    fun `Fail to sign in when email violates min length`() = testApp(false) { client ->
        val (_, password) = generateCredentials()
        val user = UserDTO(emailMinLengthViolation, password)
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<ErrorResponse>().apply {
                assertEquals(
                    ErrorResponse(
                        ErrorResponse.Type.VALIDATION,
                        ValidationReason.tooShort(UserDTO::email.name, EntityFieldLength.Users.Email.minLength)
                    ), this
                )
            }
        }
    }

    @Test
    fun `Fail to sign in when password violates min length`() = testApp(false) { client ->
        val (email, _) = generateCredentials()
        val user = UserDTO(email, passwordMinLengthViolation)
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<ErrorResponse>().apply {
                assertEquals(
                    ErrorResponse(
                        ErrorResponse.Type.VALIDATION,
                        ValidationReason.tooShort(UserDTO::password.name, EntityFieldLength.Users.Password.minLength)
                    ), this
                )
            }
        }
    }

    @Test
    fun `Fail to sign in when password violates max length`() = testApp(false) { client ->
        val (email, _) = generateCredentials()
        val user = UserDTO(email, passwordMaxLengthViolation)
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<ErrorResponse>().apply {
                assertEquals(
                    ErrorResponse(
                        ErrorResponse.Type.VALIDATION,
                        ValidationReason.tooLong(UserDTO::password.name, EntityFieldLength.Users.Password.maxLength)
                    ), this
                )
            }
        }
    }

    @Test
    fun `Fail to sign in when email violates max length`() = testApp(false) { client ->
        val (_, password) = generateCredentials()
        val user = UserDTO(emailMaxLengthViolation, password)
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<ErrorResponse>().apply {
                assertEquals(
                    ErrorResponse(
                        ErrorResponse.Type.VALIDATION,
                        ValidationReason.tooLong(UserDTO::email.name, EntityFieldLength.Users.Email.maxLength)
                    ), this
                )
            }
        }
    }


    private companion object {

        fun generatePassword() =
            "p".repeat(EntityFieldLength.Users.Password.minLength)

        fun generateEmail() =
            "e".repeat(EntityFieldLength.Users.Email.minLength)

        fun generateCredentials() = Pair(generateEmail(), generatePassword())

        const val PASSWORD_BLANK = "   "
        const val EMAIL_BLANK = "   "
        val passwordMinLengthViolation = "p".repeat(EntityFieldLength.Users.Password.minLength - 1)
        val passwordMaxLengthViolation = "p".repeat(EntityFieldLength.Users.Password.maxLength + 1)
        val emailMinLengthViolation = "e".repeat(EntityFieldLength.Users.Email.minLength - 1)
        val emailMaxLengthViolation = "e".repeat(EntityFieldLength.Users.Email.maxLength + 1)
    }
}