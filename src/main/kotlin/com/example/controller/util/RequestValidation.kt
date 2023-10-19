package com.example.controller.util

import com.example.domain.model.Chat
import com.example.domain.model.Member
import com.example.domain.model.Message
import com.example.domain.model.User
import com.example.util.EntityFieldLength
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.util.pipeline.*

private val validationReason = object {
    fun blank(field: String) = "$field field cannot be blank"
    val passwordContainUsername = "Password cannot contain username"
    private fun toLong(field: String, maxLength: Int) = "$field max accepted length is $maxLength"
    private fun toShort(field: String, minLength: Int) = "$field min accepted length is $minLength"
    fun length(field: String, currLength: Int, minLength: Int, maxLength: Int): String {
        return when {
            currLength > maxLength -> toLong(field, maxLength)
            else -> toShort(field, minLength)
        }
    }
}

fun RequestValidationConfig.validateMember() {
    validate<Member> { body ->
        when {
            body.uid.isBlank() -> ValidationResult.Invalid(validationReason.blank("uid"))
            body.name.isBlank() -> ValidationResult.Invalid(validationReason.blank("name"))
            body.name.length > EntityFieldLength.Members.Name.maxLength || body.name.length < EntityFieldLength.Members.Name.minLength -> {
                ValidationResult.Invalid(
                    validationReason.length(
                        "name",
                        body.name.length,
                        EntityFieldLength.Members.Name.minLength,
                        EntityFieldLength.Members.Name.maxLength
                    )
                )
            }

            else -> ValidationResult.Valid
        }
    }
}

fun RequestValidationConfig.validateChat() {
    validate<Chat> { body ->
        when {
            body.name.isBlank() -> ValidationResult.Invalid(validationReason.blank("name"))
            body.name.length > EntityFieldLength.Chats.Name.maxLength || body.name.length < EntityFieldLength.Chats.Name.minLength -> {
                ValidationResult.Invalid(
                    validationReason.length(
                        "name",
                        body.name.length,
                        EntityFieldLength.Chats.Name.minLength,
                        EntityFieldLength.Chats.Name.maxLength
                    )
                )
            }

            else -> ValidationResult.Valid
        }
    }
}

fun RequestValidationConfig.validateMessage() {
    validate<Message> { body ->
        when {
            body.chatId.isBlank() -> ValidationResult.Invalid(validationReason.blank("uid"))
            body.sender.isBlank() -> ValidationResult.Invalid(validationReason.blank("name"))
            body.sender.length > EntityFieldLength.Messages.Sender.maxLength || body.sender.length < EntityFieldLength.Messages.Sender.minLength -> {
                ValidationResult.Invalid(
                    validationReason.length(
                        "sender",
                        body.sender.length,
                        EntityFieldLength.Messages.Sender.minLength,
                        EntityFieldLength.Messages.Sender.maxLength
                    )
                )
            }

            body.content.isBlank() -> ValidationResult.Invalid(validationReason.blank("content"))
            else -> ValidationResult.Valid
        }
    }
}

fun RequestValidationConfig.validateUser() {
    validate<User> { body ->
        when {
            body.username.isBlank() -> ValidationResult.Invalid(validationReason.blank("username"))
            body.email.isBlank() -> ValidationResult.Invalid(validationReason.blank("email"))
            body.password.isBlank() -> ValidationResult.Invalid(validationReason.blank("password"))
            body.password.contains(body.username) -> ValidationResult.Invalid(validationReason.passwordContainUsername)
            body.username.length > EntityFieldLength.Users.Username.maxLength || body.username.length < EntityFieldLength.Users.Username.minLength -> {
                ValidationResult.Invalid(
                    validationReason.length(
                        "username",
                        body.username.length,
                        EntityFieldLength.Users.Username.minLength,
                        EntityFieldLength.Users.Username.maxLength
                    )
                )
            }

            body.email.length > EntityFieldLength.Users.Email.maxLength || body.email.length < EntityFieldLength.Users.Email.minLength -> {
                ValidationResult.Invalid(
                    validationReason.length(
                        "email",
                        body.email.length,
                        EntityFieldLength.Users.Email.minLength,
                        EntityFieldLength.Users.Email.maxLength
                    )
                )
            }

            body.password.length > EntityFieldLength.Users.Password.maxLength || body.password.length < EntityFieldLength.Users.Password.minLength -> {
                ValidationResult.Invalid(
                    validationReason.length(
                        "password",
                        body.password.length,
                        EntityFieldLength.Users.Password.minLength,
                        EntityFieldLength.Users.Password.maxLength
                    )
                )
            }

            else -> ValidationResult.Valid
        }
    }
}

fun PipelineContext<Unit, ApplicationCall>.isRequestedDataOwner(memberId: String): Boolean {
    val principal = call.principal<JWTPrincipal>()
    val loggedInUserId = principal?.payload?.getClaim("uid")

    return (loggedInUserId != null && loggedInUserId.asString() == memberId)
}