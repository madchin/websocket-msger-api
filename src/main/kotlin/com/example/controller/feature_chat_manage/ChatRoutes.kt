package com.example.controller.feature_chat_manage

import com.example.controller.util.isChatParticipant
import com.example.controller.util.isRequestedDataOwner
import com.example.domain.dao.service.ChatService
import com.example.domain.model.Chat
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
        val chat = call.receive<Chat>()
        val chatWithOwner = chat.copy(lastSeenMembers = listOf(mapOf(memberId to System.currentTimeMillis())))
        if (!isRequestedDataOwner(memberId)) {
            throw ForbiddenException
        }
        chatService.createChat(chatWithOwner).also {
            call.respond(HttpStatusCode.Created, it.toString())
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
        val chat = call.receive<Chat>()
        if (!isChatParticipant(chat)) {
            throw ForbiddenException
        }
        chatService.changeChatName(chatId, chat.name).also {
            call.respond(HttpStatusCode.OK)
        }
    }
}