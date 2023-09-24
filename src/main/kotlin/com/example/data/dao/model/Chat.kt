package com.example.data.dao.model

import kotlinx.serialization.Serializable

@Serializable
data class Chat(val id: String, val name: String, val messagesIds: List<Int>, val membersIds: List<Int>)
