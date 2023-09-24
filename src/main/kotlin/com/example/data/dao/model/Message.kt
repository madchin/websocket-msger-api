package com.example.data.dao.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val chatId: String? = null,
    val sender: String,
    val content: String,
    val timestamp: Long? = null
)
