package com.example.util

import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

sealed class ExplicitException(
    val status: HttpStatusCode = HttpStatusCode.BadRequest,
    override val message: String = "Oops. Something went wrong!"
) :
    Throwable() {
    data object ChatInsert : ExplicitException(message = "Chat has not been inserted")
    data object ChatNotFound : ExplicitException(message = "Chat has not been found")
    data object ChatUpdate : ExplicitException(message = "Chat has not been updated")
    data object MemberUpsert : ExplicitException(message = "Member has not been inserted nor updated")
    data object MemberNotFound : ExplicitException(message = "Member has not been found")
    data object MemberUpdate : ExplicitException(message = "Member has not been updated")
    data object MessageInsert : ExplicitException(message = "Message has not been inserted")
    data object MessagesNotFound : ExplicitException(message = "Messages has not been found")
    data object UserInsert : ExplicitException(message = "User has not been inserted")
    data object UserNotFound : ExplicitException(message = "User has not been found")
    data object UserUpdate : ExplicitException(message = "User has not been updated")
    data object Generic : ExplicitException()
    data object WrongCredentials : ExplicitException(message = "Provided credentials are wrong")

    data object Forbidden : ExplicitException(status = HttpStatusCode.Forbidden)

    data object DuplicateUser : ExplicitException(message = "User already exists")
}

fun StatusPagesConfig.responseExceptionHandler() {
    exception<Throwable> { call, cause ->
        if(cause is ExplicitException) {
            call.respond(cause.status, cause.message)
        }
        else {
            call.respond(ExplicitException.Generic.status, ExplicitException.Generic.message)
        }
    }
}

