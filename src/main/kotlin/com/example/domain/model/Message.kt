package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val chatId: String,
    val sender: String,
    val content: String,
    val timestamp: Long? = System.currentTimeMillis()
)
