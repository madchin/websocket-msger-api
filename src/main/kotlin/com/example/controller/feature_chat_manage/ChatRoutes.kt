package com.example.controller.feature_chat_manage

import com.example.domain.model.ChatDTO
import com.example.domain.service.ChatService
import com.example.util.ForbiddenException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.chat(chatService: ChatService) {
    get("/chat/{chatId}") {
        val chatId = call.parameters.getOrFail("chatId")
        val principal = call.principal<JWTPrincipal>()
        val userIdClaim = principal?.payload?.getClaim("uid") ?: throw ForbiddenException
        chatService.getChat(chatId, userIdClaim.asString()).also {
            call.respond(HttpStatusCode.OK, it)
        }
    }

    post("/chat") {
        val principal = call.principal<JWTPrincipal>()
        val chatDTO = call.receive<ChatDTO>()
        val userIdClaim = principal?.payload?.getClaim("uid") ?: throw ForbiddenException
        chatService.createChat(chatDTO.toChat(), userIdClaim.asString()).also {
            call.respond(HttpStatusCode.Created, it)
        }
    }
    post("/chat/{chatId}/join-chat") {
        val chatId = call.parameters.getOrFail("chatId")
        val principal = call.principal<JWTPrincipal>()
        val userIdClaim = principal?.payload?.getClaim("uid") ?: throw ForbiddenException
        chatService.joinChat(chatId, userIdClaim.asString()).also {
            call.respond(HttpStatusCode.OK)
        }
    }

    put("chat/{id}/change-name") {
        val chatId = call.parameters.getOrFail("id")
        val chatDTO = call.receive<ChatDTO>()
        val principal = call.principal<JWTPrincipal>()
        val userIdClaim = principal?.payload?.getClaim("uid") ?: throw ForbiddenException

        chatService.changeChatName(chatId, chatDTO.name, userIdClaim.asString()).also {
            call.respond(HttpStatusCode.OK)
        }
    }
}