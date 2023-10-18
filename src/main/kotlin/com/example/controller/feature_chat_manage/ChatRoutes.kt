package com.example.controller.feature_chat_manage

import com.example.controller.util.isChatParticipant
import com.example.controller.util.isRequestedDataOwner
import com.example.domain.service.ChatService
import com.example.domain.model.ChatDTO
import com.example.util.ForbiddenException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.chat(chatService: ChatService) {
    get("/chat/{chatId}?member-id={memberId}") {
        val chatId = call.parameters.getOrFail("chatId")
        val memberId = call.request.queryParameters.getOrFail("memberId")
        if (!isRequestedDataOwner(memberId)) {
            throw ForbiddenException
        }
        chatService.getChat(chatId, memberId).also {
            call.respond(HttpStatusCode.OK, it)
        }
    }

    post("/chat?member-id={memberId}") {
        val memberId = call.request.queryParameters.getOrFail("memberId")
        val chatDTO = call.receive<ChatDTO>()
        val chatDTOWithOwner = chatDTO.copy(lastSeenMembers = listOf(mapOf(memberId to System.currentTimeMillis())))
        if (!isRequestedDataOwner(memberId)) {
            throw ForbiddenException
        }
        chatService.createChat(chatDTOWithOwner.toChat()).also {
            call.respond(HttpStatusCode.Created, it)
        }
    }
    post("/chat/{chatId}/join-chat?member-id={memberId}") {
        val chatId = call.parameters.getOrFail("chatId")
        val memberId = call.request.queryParameters.getOrFail("memberId")
        if (!isRequestedDataOwner(memberId)) {
            throw ForbiddenException
        }
        chatService.joinChat(chatId, memberId).also {
            call.respond(HttpStatusCode.OK)
        }
    }

    put("chat/{id}/change-name") {
        val chatId = call.parameters.getOrFail("id")
        val chatDTO = call.receive<ChatDTO>()
        if (!isChatParticipant(chatDTO.toChat())) {
            throw ForbiddenException
        }
        chatService.changeChatName(chatId, chatDTO.name).also {
            call.respond(HttpStatusCode.OK)
        }
    }
}