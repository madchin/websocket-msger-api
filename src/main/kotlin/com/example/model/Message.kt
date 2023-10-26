package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val chatId: String,
    val sender: String,
    val content: String,
    val timestamp: Long? = System.currentTimeMillis()
)

@Serializable
data class MessageDTO(
    val sender: String,
    val content: String,
    val timestamp: Long? = System.currentTimeMillis()
) {
    fun toMessage(chatId: String) = Message(chatId, this.sender, this.content, this.timestamp)
}
