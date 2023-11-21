package com.example.controller.util

import com.example.model.ChatDTO
import com.example.model.Message
import com.example.model.UserDTO
import com.example.util.EntityFieldLength
import io.ktor.server.plugins.requestvalidation.*

object ValidationReason {
    fun blank(field: String) = "$field field cannot be blank"
    fun tooLong(field: String, maxLength: Int) = "$field max accepted length is $maxLength"
    fun tooShort(field: String, minLength: Int) = "$field min accepted length is $minLength"
}

fun String.isNotShort(minLength: Int): Boolean = this.length < minLength
fun String.isNotLong(maxLength: Int): Boolean = this.length > maxLength

fun RequestValidationConfig.validateChat() {
    validate<ChatDTO> { body ->
        val name = EntityFieldLength.Chats.Name
        when {
            body.name.isBlank() -> ValidationResult.Invalid(ValidationReason.blank(ChatDTO::name.name))
            body.name.isNotLong(name.maxLength) -> {
                ValidationResult.Invalid(ValidationReason.tooLong(ChatDTO::name.name, name.maxLength))
            }

            body.name.isNotShort(name.minLength) -> {
                ValidationResult.Invalid(ValidationReason.tooShort(ChatDTO::name.name, name.minLength))
            }

            else -> ValidationResult.Valid
        }
    }
}

fun RequestValidationConfig.validateMessage() {
    validate<Message> { body ->
        val sender = EntityFieldLength.Messages.Sender
        when {
            body.chatId.isBlank() -> ValidationResult.Invalid(ValidationReason.blank(Message::chatId.name))
            body.sender.isBlank() -> ValidationResult.Invalid(ValidationReason.blank(Message::sender.name))
            body.sender.isNotLong(sender.maxLength) -> {
                ValidationResult.Invalid(ValidationReason.tooLong(Message::sender.name, sender.maxLength))
            }

            body.sender.isNotShort(sender.minLength) -> {
                ValidationResult.Invalid(ValidationReason.tooShort(Message::sender.name, sender.minLength))
            }

            body.content.isBlank() -> ValidationResult.Invalid(ValidationReason.blank(Message::content.name))
            else -> ValidationResult.Valid
        }
    }
}

fun RequestValidationConfig.validateUser() {
    validate<UserDTO> { body ->
        val email = EntityFieldLength.Users.Email
        val password = EntityFieldLength.Users.Password
        when {
            body.email.isBlank() -> ValidationResult.Invalid(ValidationReason.blank(UserDTO::email.name))
            body.password.isBlank() -> ValidationResult.Invalid(ValidationReason.blank(UserDTO::password.name))

            body.email.isNotLong(email.maxLength) -> {
                ValidationResult.Invalid(ValidationReason.tooLong(UserDTO::email.name, email.maxLength))
            }

            body.email.isNotShort(email.minLength) -> {
                ValidationResult.Invalid(ValidationReason.tooShort(UserDTO::email.name, email.minLength))
            }

            body.password.isNotLong(password.maxLength) -> {
                ValidationResult.Invalid(ValidationReason.tooLong(UserDTO::password.name, password.maxLength))
            }

            body.password.isNotShort(password.minLength) -> {
                ValidationResult.Invalid(ValidationReason.tooShort(UserDTO::password.name, password.minLength))
            }

            else -> ValidationResult.Valid
        }
    }
}

