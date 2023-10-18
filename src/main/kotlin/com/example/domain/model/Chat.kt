package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Chat(
    val id: String?,
    val name: String,
    val messageIds: List<Int> = emptyList(),
    val lastSeenMembers: List<Map<String, Long>> = emptyList()
)

@Serializable
data class ChatDTO(
    val name: String,
    val messageIds: List<Int> = emptyList(),
    val lastSeenMembers: List<Map<String, Long>> = emptyList()
) {
    fun toChat(): Chat = Chat(id = null, name = name, messageIds = messageIds, lastSeenMembers = lastSeenMembers)
}
