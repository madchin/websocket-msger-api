package com.example.util

sealed class EntityFieldLength(val minLength: Int, val maxLength: Int) {
    object Users {
        data object Email : EntityFieldLength(3, 320)
        data object Password : EntityFieldLength(6, 64)
    }
    object Members {
        data object Name : EntityFieldLength(6, 64)
    }
    object Chats {
        data object Name : EntityFieldLength(12, 64)
    }
    object Messages {
        data object Sender : EntityFieldLength(12, 64)
    }
}