package com.example.util

enum class EntityFieldLength(val minLength: Int, val maxLength: Int) {
    USERS_USERNAME(12,64),
    USERS_EMAIL(0,320),
    USERS_PASSWORD(6,64),
    MEMBERS_NAME(6,64),
    CHATS_NAME(12,64)
}