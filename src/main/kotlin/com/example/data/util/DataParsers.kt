package com.example.data.util

fun parseUserData(userData: String): Pair<String,String> {
    val parts = userData.split(",")
    if (parts.size == 2) {
        val username = parts[0].trim()
        val password = parts[1].trim()
        return Pair(username, password)
    }
    return Pair("","")
}