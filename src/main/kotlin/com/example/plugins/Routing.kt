package com.example.plugins

import com.example.controller.feature_chat_manage.chat
import com.example.controller.feature_sign_in_up.signInUp
import com.example.domain.service.Services
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureRouting(services: Services) {
    routing {
        //signInUp(services.userService)
        authenticate("auth-jwt") {
            chat(services.chatService)
        }
        signInUp(services.authService)
    }
}
