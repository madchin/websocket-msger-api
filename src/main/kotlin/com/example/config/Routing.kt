package com.example.config

import com.example.controller.feature_chat.chat
import com.example.controller.feature_sign_in_up.signInUp
import com.example.service.AuthService
import com.example.service.ChatService
import com.example.socket.ChatMemberSocketHandler
import com.example.socket.ChatRoomSocketHandler
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    chatService: ChatService,
    authService: AuthService,
    chatMemberSocketHandler: ChatMemberSocketHandler,
    chatRoomSocketHandler: ChatRoomSocketHandler
) {
    routing {
        //signInUp(services.userService)
        authenticate("auth-jwt") {
            chat(chatService, chatMemberSocketHandler,chatRoomSocketHandler)
        }
        signInUp(authService)
    }
}
