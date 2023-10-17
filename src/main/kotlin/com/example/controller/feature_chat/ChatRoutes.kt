package com.example.controller.feature_chat

import com.example.domain.dao.service.ChatService
import com.example.domain.dao.service.MemberService
import com.example.domain.model.Chat
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.chat(chatService: ChatService, memberService: MemberService) {

    get("/chat/{chatId}?member-id={memberId}") {
        val chatId = call.parameters.getOrFail("chatId")
        val memberId = call.request.queryParameters.getOrFail("memberId")
        chatService.getChat(chatId, memberId).also {
            call.respond(HttpStatusCode.OK, it)
        }
    }

    post("/chat?member-id={memberId}") {
        val memberId = call.request.queryParameters.getOrFail("memberId")
        val chat = call.receive<Chat>()
        val chatWithOwner = chat.copy(lastSeenMembers = listOf(mapOf(memberId to System.currentTimeMillis())))
        chatService.createChat(chatWithOwner).also {
            call.respond(HttpStatusCode.Created, it.toString())
        }
    }
    post("/chat/{chatId}/join-chat?member-id={memberId}") {
        val chatId = call.parameters.getOrFail("chatId")
        val memberId = call.request.queryParameters.getOrFail("memberId")
        val member = memberService.getMember(memberId)
        chatService.joinChat(chatId, member.uid).also {
            call.respond(HttpStatusCode.OK)
        }
    }

    put("chat/{id}/change-name") {
        val chatId = call.parameters.getOrFail("id")
        val chat = call.receive<Chat>()
        chatService.changeChatName(chatId, chat.name).also {
            call.respond(HttpStatusCode.OK)
        }
    }
}