package com.example.controller.util

import com.example.data.util.EntityFieldLength
import com.example.domain.model.Chat
import com.example.domain.model.Member
import com.example.domain.model.Message
import com.example.domain.model.User
import io.ktor.server.plugins.requestvalidation.*

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
            body.name.length > EntityFieldLength.MEMBERS_NAME.maxLength || body.name.length < EntityFieldLength.MEMBERS_NAME.minLength -> {
                ValidationResult.Invalid(
                    validationReason.length(
                        "name",
                        body.name.length,
                        EntityFieldLength.MEMBERS_NAME.minLength,
                        EntityFieldLength.MEMBERS_NAME.maxLength
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
            body.name.length > EntityFieldLength.CHATS_NAME.maxLength || body.name.length < EntityFieldLength.CHATS_NAME.minLength -> {
                ValidationResult.Invalid(
                    validationReason.length(
                        "name",
                        body.name.length,
                        EntityFieldLength.CHATS_NAME.minLength,
                        EntityFieldLength.CHATS_NAME.maxLength
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
            body.sender.length > EntityFieldLength.MEMBERS_NAME.maxLength || body.sender.length < EntityFieldLength.MEMBERS_NAME.minLength -> {
                ValidationResult.Invalid(
                    validationReason.length(
                        "sender",
                        body.sender.length,
                        EntityFieldLength.MEMBERS_NAME.minLength,
                        EntityFieldLength.MEMBERS_NAME.maxLength
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
            body.username.length > EntityFieldLength.USERS_USERNAME.maxLength || body.username.length < EntityFieldLength.USERS_USERNAME.minLength -> {
                ValidationResult.Invalid(
                    validationReason.length(
                        "username",
                        body.username.length,
                        EntityFieldLength.USERS_USERNAME.minLength,
                        EntityFieldLength.USERS_USERNAME.maxLength
                    )
                )
            }

            body.email.length > EntityFieldLength.USERS_EMAIL.maxLength || body.email.length < EntityFieldLength.USERS_EMAIL.minLength -> {
                ValidationResult.Invalid(
                    validationReason.length(
                        "email",
                        body.email.length,
                        EntityFieldLength.USERS_EMAIL.minLength,
                        EntityFieldLength.USERS_EMAIL.maxLength
                    )
                )
            }

            body.password.length > EntityFieldLength.USERS_PASSWORD.maxLength || body.password.length < EntityFieldLength.USERS_PASSWORD.minLength -> {
                ValidationResult.Invalid(
                    validationReason.length(
                        "password",
                        body.password.length,
                        EntityFieldLength.USERS_PASSWORD.minLength,
                        EntityFieldLength.USERS_PASSWORD.maxLength
                    )
                )
            }

            else -> ValidationResult.Valid
        }
    }
}