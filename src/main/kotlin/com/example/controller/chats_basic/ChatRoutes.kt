package com.example.controller.chats_basic

import com.example.domain.dao.service.ChatService
import com.example.domain.model.Chat
import com.example.domain.model.Member
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.chat(chatService: ChatService) {
    post("/chats") {
        val tmpChat = Chat(
            name = "nameasdasdasd",
            messageIds = listOf(1, 2, 3),
            lastSeenMembers = listOf(mapOf("xd" to 123312, "elo" to 11))
        )
        chatService.createChat(tmpChat).also {
            call.respond(HttpStatusCode.Created, it.toString())
        }
    }
    post("/chats/{id}/add-member") {
        val chatId = call.parameters.getOrFail("id")
        val member = call.receive<Member>()
        chatService.joinChat(chatId, member.uid).also {
            call.respond(HttpStatusCode.OK)
        }
    }

    post("/chats/{id}/last-seen2") {
        val chatId = call.parameters.getOrFail("id")
        chatService.joinChat(chatId = chatId, memberUid = "chasduj").also {
            call.respond(HttpStatusCode.OK)
        }
    }

    delete("/chats/{id}") {
        val chatId = call.parameters.getOrFail("id")
        chatService.deleteChat(chatId).also {
            call.respond(HttpStatusCode.OK)
        }
    }
    get("/chats/{id}") {
        val chatId = call.parameters.getOrFail("id")
        chatService.getChat(chatId).also {
            call.respond(HttpStatusCode.OK, it)
        }
    }
    put("chats/{id}") {
        val chatId = call.parameters.getOrFail("id")
        val chat = call.receive<Chat>()
        chatService.changeChatName(chatId, chat.name).also {
            call.respond(HttpStatusCode.OK)
        }
    }
}