package com.example.config.plugin

import com.example.controller.feature_chat_manage.chat
import com.example.controller.feature_sign_in_up.signInUp
import com.example.service.AuthService
import com.example.service.ChatService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureRouting(chatService: ChatService, authService: AuthService) {
    routing {
        //signInUp(services.userService)
        authenticate("auth-jwt") {
            chat(chatService)
        }
        signInUp(authService)
    }
}
