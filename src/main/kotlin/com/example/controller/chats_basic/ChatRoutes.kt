package com.example.controller.chats_basic

import com.example.controller.util.ErrorResponse
import com.example.controller.util.ErrorType
import com.example.data.dao.model.Chat
import com.example.data.service.ChatService
import com.example.data.util.GenericException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
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
        val result = chatService.createChat(tmpChat)

        result.onSuccess {
            call.respond(HttpStatusCode.Created, it.toString())
        }

        result.onFailure {
            val message = it.message ?: GenericException().message
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(ErrorType.NOT_FOUND.name, message))
        }
    }
    post("/chats/{id}/add-member") {
        try {
            val chatId = call.parameters.getOrFail("id")
            val result = chatService.updateLastSeenMembers(chatId = chatId, memberUid = "chuj", lastSeen = 123123123123)
            result.onSuccess {
                call.respond(HttpStatusCode.OK)
            }
            result.onFailure {
                val message = it.message ?: GenericException().message
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(ErrorType.GENERIC.name, message))
            }
        } catch (e: MissingRequestParameterException) {
            call.respond(HttpStatusCode.BadRequest, e.message.toString())

        }
    }

    post("/chats/{id}/last-seen2") {
        try {
            val chatId = call.parameters.getOrFail("id")
            val result =
                chatService.updateLastSeenMembers(chatId = chatId, memberUid = "chasduj", lastSeen = 123123123123)
            result.onSuccess {
                call.respond(HttpStatusCode.OK)
            }
            result.onFailure {
                val message = it.message ?: GenericException().message
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(ErrorType.GENERIC.name, message))
            }
        } catch (e: MissingRequestParameterException) {
            call.respond(HttpStatusCode.BadRequest, e.message.toString())

        }
    }

    delete("/chats/{id}") {
        try {
            val chatId = call.parameters.getOrFail("id")
            val result = chatService.deleteChat(chatId)
            result.onSuccess {
                call.respond(HttpStatusCode.OK)
            }
            result.onFailure {
                val message = it.message ?: GenericException().message
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(ErrorType.GENERIC.name, message))
            }
        } catch (e: MissingRequestParameterException) {
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
        }
    }
    get("/chats/{id}") {
        try {
            val chatId = call.parameters.getOrFail("id")
            val result = chatService.getChat(chatId)
            result.onSuccess {
                call.respond(HttpStatusCode.OK, it)
            }
            result.onFailure {
                val message = it.message ?: GenericException().message
                call.respond(HttpStatusCode.BadRequest, message)
            }
        } catch (e: MissingRequestParameterException) {
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
        }
    }
    put("chats/{id}") {
        try {
            val chatId = call.parameters.getOrFail("id")
            val chat = call.receive<Chat>()
            val result = chatService.changeChatName(chatId, chat.name)
            result.onSuccess {
                call.respond(HttpStatusCode.OK)
            }
            result.onFailure {
                val message = it.message ?: GenericException().message
                call.respond(HttpStatusCode.BadRequest, message)
            }
        } catch (e: MissingRequestParameterException) {
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
        }
    }
}