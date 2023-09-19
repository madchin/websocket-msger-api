package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Chat(val id: String, val name: String, val members: List<String>, val messages: List<String>? = null)
