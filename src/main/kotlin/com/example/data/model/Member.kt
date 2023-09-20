package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Member(val uid: String, val name: String, val lastSeen: Map<String, Long>)
