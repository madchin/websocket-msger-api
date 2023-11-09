package com.example.controller.util

import com.example.util.ExplicitException
import io.ktor.http.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val type: String, val message: String) {
    object Type {
        const val VALIDATION = "Validation"
    }
}

fun StatusPagesConfig.responseExceptionHandler() {
    exception<Throwable> { call, cause ->
        when (cause) {
            is ExplicitException -> call.respond(cause.status, ErrorResponse(cause.status.description, cause.message))
            else -> {
                val genericError = ExplicitException.Generic
                call.respond(
                    status = genericError.status,
                    ErrorResponse(genericError.description, genericError.message)
                )
            }
        }
    }
}

fun StatusPagesConfig.requestValidationExceptionHandler() {
    exception<RequestValidationException> { call, cause ->
        call.respond(
            HttpStatusCode.BadRequest,
            ErrorResponse(ErrorResponse.Type.VALIDATION, cause.reasons.joinToString())
        )
    }
}