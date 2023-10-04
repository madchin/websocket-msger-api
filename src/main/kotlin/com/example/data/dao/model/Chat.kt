package com.example.data.dao.model

import kotlinx.serialization.Serializable

@Serializable
data class Chat(
    val id: String? = null,
    val name: String,
    val messageIds: List<Int> = emptyList(),
    val lastSeenMembers: List<Map<String, Long>> = emptyList()
)
