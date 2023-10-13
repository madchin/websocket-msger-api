package com.example.data.socket

import com.example.data.util.GenericException
import com.example.domain.dao.service.ChatService
import com.example.domain.dao.service.MemberChatService
import com.example.domain.dao.service.MessageService
import com.example.domain.model.Chat
import com.example.domain.model.ChatMember
import com.example.domain.model.Message
import com.example.domain.socket.ChatSocketHandler
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class ChatSocketHandlerImpl(
    private val memberChatService: MemberChatService,
    val messageService: MessageService,
    val chatService: ChatService
) : ChatSocketHandler {
    override val chatMembers: MutableSet<ChatMember> = Collections.synchronizedSet(LinkedHashSet())
    override suspend fun sendMessage(message: Message): Result<Boolean> =
        messageService.sendMessage(message).run {
            onSuccess {
                val parsedMessage = Json.encodeToString(message)
                chatMembers.forEach { chatMember ->
                    chatMember.session.send(Frame.Text(parsedMessage))
                }
            }
        }

    override suspend fun joinChat(chatId: String, chatMember: ChatMember): Result<Chat> {
        val permitToJoinChat = memberChatService.canJoinChat(memberId = chatMember.member.uid, chatId = chatId)
        return when {
            permitToJoinChat.isSuccess -> {
                chatService
                    .joinChat(chatId = chatId, memberUid = chatMember.member.uid)
                    .run {
                        onSuccess {
                            chatMembers.add(chatMember)
                            return Result.success(it)
                        }
                        onFailure {
                            return Result.failure(it)
                        }
                    }
            }
            permitToJoinChat.isFailure -> {
                return Result.failure(permitToJoinChat.exceptionOrNull()!!)
            }
            else -> Result.failure(GenericException())
        }
    }
}
