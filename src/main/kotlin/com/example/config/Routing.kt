package com.example.config

import com.example.controller.feature_chat.chat
import com.example.controller.feature_member_manage.member
import com.example.controller.feature_sign_in_up.signInUp
import com.example.service.AuthService
import com.example.service.ChatService
import com.example.service.MemberService
import com.example.socket.ChatMemberSocketHandler
import com.example.socket.ChatRoomSocketHandler
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    chatService: ChatService,
    authService: AuthService,
    memberService: MemberService,
    chatMemberSocketHandler: ChatMemberSocketHandler,
    chatRoomSocketHandler: ChatRoomSocketHandler
) {
    routing {
        authenticate("auth-jwt") {
            chat(chatService, chatMemberSocketHandler, chatRoomSocketHandler)
            member(memberService)
        }
        signInUp(authService)
    }
}
