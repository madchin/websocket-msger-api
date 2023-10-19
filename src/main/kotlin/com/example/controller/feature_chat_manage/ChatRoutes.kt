package com.example.controller.feature_chat_manage

import com.example.domain.model.ChatDTO
import com.example.domain.service.ChatService
import com.example.util.ExplicitException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.chat(chatService: ChatService) {
    get("/chat/{id}") {
        val chatId = call.parameters.getOrFail("id")
        val principal = call.principal<JWTPrincipal>()
        val userIdClaim = principal?.payload?.getClaim("uid") ?: throw ExplicitException.Forbidden

        chatService.getChat(chatId, userIdClaim.asString()).also {
            call.respond(HttpStatusCode.OK, it)
        }
    }

    post("/chat") {
        val principal = call.principal<JWTPrincipal>()
        val chatDTO = call.receive<ChatDTO>()
        val userIdClaim = principal?.payload?.getClaim("uid") ?: throw ExplicitException.Forbidden

        chatService.createChat(chatDTO, userIdClaim.asString()).also {
            call.respond(HttpStatusCode.Created, it)
        }
    }
    post("/chat/{id}/join-chat") {
        val chatId = call.parameters.getOrFail("id")
        val principal = call.principal<JWTPrincipal>()
        val userIdClaim = principal?.payload?.getClaim("uid") ?: throw ExplicitException.Forbidden

        chatService.joinChat(chatId, userIdClaim.asString()).also {
            call.respond(HttpStatusCode.OK)
        }
    }

    put("chat/{id}/change-name") {
        val chatId = call.parameters.getOrFail("id")
        val chatDTO = call.receive<ChatDTO>()
        val principal = call.principal<JWTPrincipal>()
        val userIdClaim = principal?.payload?.getClaim("uid") ?: throw ExplicitException.Forbidden

        chatService.changeChatName(chatId, chatDTO.name, userIdClaim.asString()).also {
            call.respond(HttpStatusCode.OK)
        }
    }
}