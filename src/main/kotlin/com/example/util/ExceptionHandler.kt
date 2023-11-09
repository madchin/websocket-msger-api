package com.example.util

import io.ktor.http.*

sealed class ExplicitException(
    val status: HttpStatusCode = HttpStatusCode.BadRequest,
    override val message: String = "Oops. Something went wrong!"
) :
    Throwable() {
    data object ChatInsert : ExplicitException(message = "Chat has not been inserted")
    data object ChatNotFound : ExplicitException(status = HttpStatusCode.NotFound, message = "Chat has not been found")
    data object MemberInsert : ExplicitException(message = "Member has not been inserted nor updated")
    data object MemberNotFound :
        ExplicitException(status = HttpStatusCode.NotFound, message = "Member has not been found")

    data object DuplicateMember : ExplicitException(message = "Member already exists")

    data object MessageInsert : ExplicitException(message = "Message has not been inserted")
    data object MessagesNotFound :
        ExplicitException(status = HttpStatusCode.NotFound, message = "Messages has not been found")

    data object UserInsert : ExplicitException(message = "User has not been inserted")
    data object UserNotFound : ExplicitException(status = HttpStatusCode.NotFound, message = "User has not been found")
    data object Generic : ExplicitException() {
        val description = super.status.description
    }

    data object WrongCredentials : ExplicitException(message = "Provided credentials are wrong")

    data object Forbidden : ExplicitException(status = HttpStatusCode.Forbidden)

    data object DuplicateUser : ExplicitException(message = "User already exists")

    data object Unauthorized : ExplicitException(status = HttpStatusCode.Unauthorized, message = "Unauthorized")
}

