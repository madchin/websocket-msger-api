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
    post("/chats/create-chat") {
        val tmpChat = Chat(name = "nameasdasdasd", messageIds = listOf(1,2,3), memberIds = listOf(1,2))
        val result = chatService.createChat(tmpChat)
        when {
            result.isSuccess -> {
                call.respond(HttpStatusCode.Created, result)
            }

            result.isFailure -> {
                val message = result.exceptionOrNull()?.message ?: GenericException().message
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(ErrorType.NOT_FOUND.name, message))
            }
        }
    }
    delete("/chats/delete-chat/{id}") {
        try {
            val chatId = call.parameters.getOrFail("id")
            val deletionResult = chatService.deleteChat(chatId)
            when {
                deletionResult.isSuccess -> {
                    call.respond(HttpStatusCode.OK)
                }
                deletionResult.isFailure -> {
                    val message = deletionResult.exceptionOrNull()?.message ?: GenericException().message
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse(ErrorType.GENERIC.name, message))
                }
            }
        }
        catch (e: MissingRequestParameterException) {
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
        }
    }
    get("/chats/{id}") {
        try {
            val chatId = call.parameters.getOrFail("id")
            val result = chatService.getChat(chatId)
            when {
                result.isSuccess -> {
                    call.respond(HttpStatusCode.OK, result)
                }
                result.isFailure -> {
                    val message = result.exceptionOrNull()?.message ?: GenericException().message
                    call.respond(HttpStatusCode.BadRequest, message)
                }
            }
        }
        catch (e: MissingRequestParameterException) {
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
        }
    }
    put("chats/{id}") {
        try {
            val chatId = call.parameters.getOrFail("id")
            val chat = call.receive<Chat>()
            val result = chatService.changeChatName(chatId, chat.name)
            when {
                result.isSuccess -> {
                    call.respond(HttpStatusCode.OK)
                }
                result.isFailure -> {
                    val message = result.exceptionOrNull()?.message ?: GenericException().message
                    call.respond(HttpStatusCode.BadRequest, message)
                }
            }
        }
        catch (e: MissingRequestParameterException) {
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
        }
    }
}