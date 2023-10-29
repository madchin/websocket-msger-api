package com.example.controller.feature_chat

import com.example.model.ChatDTO
import com.example.service.ChatService
import com.example.socket.ChatRoomSocketHandler
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import io.ktor.server.websocket.*

fun Route.socketChat(
    chatRoomSocketHandler: ChatRoomSocketHandler
) {
    webSocket("/chat/socket/{id}") {
        val memberSession = this
        val chatId = call.parameters.getOrFail("id")
        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.payload?.getClaim("uid")?.asString()!!

        try {
            chatRoomSocketHandler.onJoin(memberSession, userId, chatId)
            for (frame in incoming) {
                chatRoomSocketHandler.broadcastMessage(chatId, frame)
            }
        } catch (e: Exception) {
            chatRoomSocketHandler.onLeave(memberSession, userId)
            throw e
        } finally {
            chatRoomSocketHandler.onLeave(memberSession, userId)
        }

    }
}

fun Route.chat(
    chatService: ChatService,
) {
    get("/chat/{id}") {
        val chatId = call.parameters.getOrFail("id")
        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.payload?.getClaim("uid")?.asString()!!

        chatService.getChat(chatId, userId).also {
            call.respond(HttpStatusCode.OK, it)
        }
    }

    post("/chat") {
        val principal = call.principal<JWTPrincipal>()
        val chatDTO = call.receive<ChatDTO>()
        val userId = principal?.payload?.getClaim("uid")?.asString()!!

        chatService.createChat(chatDTO, userId).also {
            call.respond(HttpStatusCode.Created, it)
        }
    }
    post("/chat/{id}/join-chat") {
        val chatId = call.parameters.getOrFail("id")
        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.payload?.getClaim("uid")?.asString()!!

        chatService.joinChat(chatId, userId).also {
            call.respond(HttpStatusCode.OK, it)
        }
    }

    put("chat/{id}/change-name") {
        val chatId = call.parameters.getOrFail("id")
        val chatDTO = call.receive<ChatDTO>()
        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.payload?.getClaim("uid")?.asString()!!

        chatService.changeChatName(chatId, chatDTO.name, userId).also {
            call.respond(HttpStatusCode.OK, it)
        }
    }

    delete("chat/{id}") {
        val chatId = call.parameters.getOrFail("id")
        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.payload?.getClaim("uid")?.asString()!!

        chatService.deleteChat(chatId, userId).also {
            call.respond(HttpStatusCode.NoContent)
        }
    }
}