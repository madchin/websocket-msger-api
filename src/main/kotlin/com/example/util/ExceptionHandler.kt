package com.example.util

import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

sealed class ExplicitException(
    val status: HttpStatusCode = HttpStatusCode.BadRequest,
    override val message: String = "Oops. Something went wrong!"
) :
    Throwable() {
    data object ChatInsert : ExplicitException(message = "Chat has not been inserted")
    data object ChatNotFound : ExplicitException(status = HttpStatusCode.NotFound, message = "Chat has not been found")
    data object ChatUpdate : ExplicitException(message = "Chat has not been updated")
    data object MemberUpsert : ExplicitException(message = "Member has not been inserted nor updated")
    data object MemberNotFound : ExplicitException(status = HttpStatusCode.NotFound, message = "Member has not been found")
    data object MemberUpdate : ExplicitException(message = "Member has not been updated")
    data object MessageInsert : ExplicitException(message = "Message has not been inserted")
    data object MessagesNotFound : ExplicitException(status = HttpStatusCode.NotFound, message = "Messages has not been found")
    data object UserInsert : ExplicitException(message = "User has not been inserted")
    data object UserNotFound : ExplicitException(status = HttpStatusCode.NotFound, message = "User has not been found")
    data object UserUpdate : ExplicitException(message = "User has not been updated")
    data object Generic : ExplicitException() {
        val description = super.status.description
    }

    data object WrongCredentials : ExplicitException(message = "Provided credentials are wrong")

    data object Forbidden : ExplicitException(status = HttpStatusCode.Forbidden)

    data object DuplicateUser : ExplicitException(message = "User already exists")
}

@Serializable
data class ErrorResponse(val type: String, val message: String)

fun StatusPagesConfig.responseExceptionHandler() {
    exception<Throwable> { call, cause ->
        if (cause is ExplicitException) {
            call.respond(cause.status, ErrorResponse(cause.status.description, cause.message))
        } else {
            val genericError = ExplicitException.Generic
            call.respond(status = genericError.status, ErrorResponse(genericError.description, genericError.message))
        }
    }
}

