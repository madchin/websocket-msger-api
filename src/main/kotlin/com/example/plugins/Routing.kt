package com.example.plugins

import com.example.controller.chats_basic.chat
import com.example.controller.feature_sign_in_up.signInUp
import com.example.domain.dao.Services
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(services: Services) {
    routing {
        //signInUp(services.userService)
        chat(services.chatService)
        signInUp(services.userService)
    }
}
