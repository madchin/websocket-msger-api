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

class SignUpRoutesTest {
    @Test
    fun `Fail to sign up when user with same username already exist`() = testApp { client ->
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
    fun `Successfully sign up`() = testApp(false) { client ->
        val (username, email, password) = generateCredentials()
        client.post("/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(UserDTO(username, email, password))
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
        }
    }


    @Test
    fun `Fail to sign up when username is blank`() = testApp(false) { client ->
        val (_, email, password) = generateCredentials()
        val user = UserDTO(USERNAME_BLANK, email, password)
        client.post("/sign-up") {
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
    fun `Fail to sign up when password is blank`() = testApp(false) { client ->
        val (username, email, _) = generateCredentials()
        val user = UserDTO(username, email, PASSWORD_BLANK)
        client.post("/sign-up") {
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
    fun `Fail to sign up when email is blank`() = testApp(false) { client ->
        val (username, _, password) = generateCredentials()
        val user = UserDTO(username, EMAIL_BLANK, password)
        client.post("/sign-up") {
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
    fun `Fail to sign up when username violates min length`() = testApp(false) { client ->
        val (_, email, password) = generateCredentials()
        val user = UserDTO(usernameMinLengthViolation, email, password)
        client.post("/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<String>().apply {
                assertEquals(
                    ValidationReason.tooShort(UserDTO::username.name, EntityFieldLength.Users.Username.minLength),
                    this
                )
            }
        }
    }

    @Test
    fun `Fail to sign up when email violates min length`() = testApp(false) { client ->
        val (username, _, password) = generateCredentials()
        val user = UserDTO(username, emailMinLengthViolation, password)
        client.post("/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<String>().apply {
                assertEquals(
                    ValidationReason.tooShort(UserDTO::email.name, EntityFieldLength.Users.Email.minLength),
                    this
                )
            }
        }
    }

    @Test
    fun `Fail to sign up when password violates min length`() = testApp(false) { client ->
        val (username, email, _) = generateCredentials()
        val user = UserDTO(username, email, passwordMinLengthViolation)
        client.post("/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<String>().apply {
                assertEquals(
                    ValidationReason.tooShort(UserDTO::password.name, EntityFieldLength.Users.Password.minLength),
                    this
                )
            }
        }
    }

    @Test
    fun `Fail to sign up when username violates max length`() = testApp(false) { client ->
        val (_, email, password) = generateCredentials()
        val user = UserDTO(usernameMaxLengthViolation, email, password)
        client.post("/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<String>().apply {
                assertEquals(
                    ValidationReason.tooLong(UserDTO::username.name, EntityFieldLength.Users.Username.maxLength),
                    this
                )
            }
        }
    }

    @Test
    fun `Fail to sign up when password violates max length`() = testApp(false) { client ->
        val (username, email, _) = generateCredentials()
        val user = UserDTO(username, email, passwordMaxLengthViolation)
        client.post("/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<String>().apply {
                assertEquals(
                    ValidationReason.tooLong(UserDTO::password.name, EntityFieldLength.Users.Password.maxLength),
                    this
                )
            }
        }
    }

    @Test
    fun `Fail to sign up when email violates max length`() = testApp(false) { client ->
        val (username, _, password) = generateCredentials()
        val user = UserDTO(username, emailMaxLengthViolation, password)
        client.post("/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            body<String>().apply {
                assertEquals(
                    ValidationReason.tooLong(UserDTO::email.name, EntityFieldLength.Users.Email.maxLength),
                    this
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