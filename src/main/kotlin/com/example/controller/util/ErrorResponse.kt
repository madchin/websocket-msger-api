package com.example.controller.util

import kotlinx.serialization.Serializable

enum class ErrorType {
    VALIDATION, NOT_FOUND
}
@Serializable
data class ErrorResponse(val type: String, val message: String)