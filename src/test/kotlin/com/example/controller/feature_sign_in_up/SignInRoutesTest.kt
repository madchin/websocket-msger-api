package com.example.controller.feature_sign_in_up

import com.example.controller.test_util.testApp
import com.example.controller.util.ValidationReason
import com.example.model.UserDTO
import com.example.service.ServiceFactory
import com.example.util.EntityFieldLength
import com.example.util.ErrorResponse
import com.example.util.ExplicitException
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

typealias SignInResponse = HashMap<String, String>


class SignInRoutesTest {
    @Test
    fun `Fail to sign in when user not exist`() = testApp(false) { client ->
        val (username, email, password) = generateCredentials()
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(UserDTO(username, email, password))
        }.apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun `Fail to sign in when wrong password provided`() = testApp { client ->
        val (username, email, password) = generateCredentials()
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
    fun `Successfully sign in`() = testApp { client ->
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

    @Test
    fun `Fail to sign in when username is blank`() = testApp(false) { client ->
        val (_, email, password) = generateCredentials()
        val user = UserDTO(USERNAME_BLANK, email, password)
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<String>().apply {
                assertEquals(this, ValidationReason.blank(UserDTO::username.name))
            }
        }
    }

    @Test
    fun `Fail to sign in when password is blank`() = testApp(false) { client ->
        val (username, email, _) = generateCredentials()
        val user = UserDTO(username, email, PASSWORD_BLANK)
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<String>().apply {
                assertEquals(this, ValidationReason.blank(UserDTO::password.name))
            }
        }
    }

    @Test
    fun `Fail to sign in when email is blank`() = testApp(false) { client ->
        val (username, _, password) = generateCredentials()
        val user = UserDTO(username, EMAIL_BLANK, password)
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<String>().apply {
                assertEquals(this, ValidationReason.blank(UserDTO::email.name))
            }
        }
    }

    @Test
    fun `Fail to sign in when username violates min length`() = testApp(false) { client ->
        val (_, email, password) = generateCredentials()
        val user = UserDTO(usernameMinLengthViolation, email, password)
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<String>().apply {
                assertEquals(
                    this, ValidationReason.length(
                        UserDTO::username.name,
                        usernameMinLengthViolation.length,
                        EntityFieldLength.Users.Username.minLength,
                        EntityFieldLength.Users.Username.maxLength
                    )
                )
            }
        }
    }

    @Test
    fun `Fail to sign in when email violates min length`() = testApp(false) { client ->
        val (username, _, password) = generateCredentials()
        val user = UserDTO(username, emailMinLengthViolation, password)
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<String>().apply {
                assertEquals(
                    this, ValidationReason.length(
                        UserDTO::email.name,
                        emailMinLengthViolation.length,
                        EntityFieldLength.Users.Email.minLength,
                        EntityFieldLength.Users.Email.maxLength
                    )
                )
            }
        }
    }

    @Test
    fun `Fail to sign in when password violates min length`() = testApp(false) { client ->
        val (username, email, _) = generateCredentials()
        val user = UserDTO(username, email, passwordMinLengthViolation)
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<String>().apply {
                assertEquals(
                    this, ValidationReason.length(
                        UserDTO::password.name,
                        passwordMinLengthViolation.length,
                        EntityFieldLength.Users.Password.minLength,
                        EntityFieldLength.Users.Password.maxLength
                    )
                )
            }
        }
    }

    @Test
    fun `Fail to sign in when username violates max length`() = testApp(false) { client ->
        val (_, email, password) = generateCredentials()
        val user = UserDTO(usernameMaxLengthViolation, email, password)
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<String>().apply {
                assertEquals(
                    this, ValidationReason.length(
                        UserDTO::username.name,
                        usernameMaxLengthViolation.length,
                        EntityFieldLength.Users.Username.minLength,
                        EntityFieldLength.Users.Username.maxLength
                    )
                )
            }
        }
    }

    @Test
    fun `Fail to sign in when password violates max length`() = testApp(false) { client ->
        val (username, email, _) = generateCredentials()
        val user = UserDTO(username, email, passwordMaxLengthViolation)
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<String>().apply {
                assertEquals(
                    this, ValidationReason.length(
                        UserDTO::password.name,
                        passwordMaxLengthViolation.length,
                        EntityFieldLength.Users.Password.minLength,
                        EntityFieldLength.Users.Password.maxLength
                    )
                )
            }
        }
    }

    @Test
    fun `Fail to sign in when email violates max length`() = testApp(false) { client ->
        val (username, _, password) = generateCredentials()
        val user = UserDTO(username, emailMaxLengthViolation, password)
        client.post("/sign-in") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<String>().apply {
                assertEquals(
                    this, ValidationReason.length(
                        UserDTO::email.name,
                        emailMaxLengthViolation.length,
                        EntityFieldLength.Users.Email.minLength,
                        EntityFieldLength.Users.Email.maxLength
                    )
                )
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

        fun generateUsername() =
            "u".repeat(EntityFieldLength.Users.Username.minLength - usernameCounter.toString().length) + usernameCounter

        fun generatePassword() =
            "p".repeat(EntityFieldLength.Users.Password.minLength - passwordCounter.toString().length) + passwordCounter

        fun generateEmail() =
            "e".repeat(EntityFieldLength.Users.Email.minLength - emailCounter.toString().length) + emailCounter

        fun generateCredentials() = Triple(generateUsername(), generateEmail(), generatePassword())

        const val USERNAME_BLANK = "   "
        const val PASSWORD_BLANK = "   "
        const val EMAIL_BLANK = "   "
        val usernameMinLengthViolation = "u".repeat(EntityFieldLength.Users.Username.minLength - 1)
        val usernameMaxLengthViolation = "u".repeat(EntityFieldLength.Users.Username.maxLength + 1)
        val passwordMinLengthViolation = "p".repeat(EntityFieldLength.Users.Password.minLength - 1)
        val passwordMaxLengthViolation = "p".repeat(EntityFieldLength.Users.Password.maxLength + 1)
        val emailMinLengthViolation = "e".repeat(EntityFieldLength.Users.Email.minLength - 1)
        val emailMaxLengthViolation = "e".repeat(EntityFieldLength.Users.Email.maxLength + 1)
    }
}