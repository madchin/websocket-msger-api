package com.example.controller.util

import com.example.model.ChatDTO
import com.example.model.Message
import com.example.model.UserDTO
import com.example.util.EntityFieldLength
import io.ktor.http.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

object ValidationReason {
    fun blank(field: String) = "$field field cannot be blank"
    const val passwordContainUsername = "Password cannot contain username"
    private fun toLong(field: String, maxLength: Int) = "$field max accepted length is $maxLength"
    private fun toShort(field: String, minLength: Int) = "$field min accepted length is $minLength"
    fun length(field: String, currLength: Int, minLength: Int, maxLength: Int): String {
        return when {
            currLength > maxLength -> toLong(field, maxLength)
            else -> toShort(field, minLength)
        }
    }
}

fun RequestValidationConfig.validateChat() {
    validate<ChatDTO> { body ->
        when {
            body.name.isBlank() -> ValidationResult.Invalid(ValidationReason.blank("name"))
            body.name.length > EntityFieldLength.Chats.Name.maxLength || body.name.length < EntityFieldLength.Chats.Name.minLength -> {
                ValidationResult.Invalid(
                    ValidationReason.length(
                        ChatDTO::name.name,
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
            body.chatId.isBlank() -> ValidationResult.Invalid(ValidationReason.blank("uid"))
            body.sender.isBlank() -> ValidationResult.Invalid(ValidationReason.blank("name"))
            body.sender.length > EntityFieldLength.Messages.Sender.maxLength || body.sender.length < EntityFieldLength.Messages.Sender.minLength -> {
                ValidationResult.Invalid(
                    ValidationReason.length(
                        Message::sender.name,
                        body.sender.length,
                        EntityFieldLength.Messages.Sender.minLength,
                        EntityFieldLength.Messages.Sender.maxLength
                    )
                )
            }

            body.content.isBlank() -> ValidationResult.Invalid(ValidationReason.blank("content"))
            else -> ValidationResult.Valid
        }
    }
}

fun RequestValidationConfig.validateUser() {
    validate<UserDTO> { body ->
        when {
            body.username.isBlank() -> ValidationResult.Invalid(ValidationReason.blank("username"))
            body.email.isBlank() -> ValidationResult.Invalid(ValidationReason.blank("email"))
            body.password.isBlank() -> ValidationResult.Invalid(ValidationReason.blank("password"))
            body.password.contains(body.username) -> ValidationResult.Invalid(ValidationReason.passwordContainUsername)
            body.username.length > EntityFieldLength.Users.Username.maxLength || body.username.length < EntityFieldLength.Users.Username.minLength -> {
                ValidationResult.Invalid(
                    ValidationReason.length(
                        UserDTO::username.name,
                        body.username.length,
                        EntityFieldLength.Users.Username.minLength,
                        EntityFieldLength.Users.Username.maxLength
                    )
                )
            }

            body.email.length > EntityFieldLength.Users.Email.maxLength || body.email.length < EntityFieldLength.Users.Email.minLength -> {
                ValidationResult.Invalid(
                    ValidationReason.length(
                        UserDTO::email.name,
                        body.email.length,
                        EntityFieldLength.Users.Email.minLength,
                        EntityFieldLength.Users.Email.maxLength
                    )
                )
            }

            body.password.length > EntityFieldLength.Users.Password.maxLength || body.password.length < EntityFieldLength.Users.Password.minLength -> {
                ValidationResult.Invalid(
                    ValidationReason.length(
                        UserDTO::password.name,
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

fun StatusPagesConfig.requestValidationExceptionHandler() {
    exception<RequestValidationException> { call, cause ->
        call.respond(HttpStatusCode.BadRequest, cause.reasons.joinToString())
    }
}
