package com.example.socket

import com.example.model.ChatMember
import com.example.model.MessageDTO
import com.example.service.ChatService
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class ChatRoomSocketHandlerImpl(
    private val chatService: ChatService
) : ChatRoomSocketHandler {
    override val chatMembers: MutableSet<ChatMember> = Collections.synchronizedSet(LinkedHashSet())

    override fun onJoin(chatMember: ChatMember) {
        chatMembers.add(chatMember)
    }

    override suspend fun broadcastMessage(chatId: String, frame: Frame) {
        if (frame is Frame.Text) {
            val decodedMessage: MessageDTO = Json.decodeFromString(frame.readText())
            chatService.sendMessage(decodedMessage.toMessage(chatId))
            val encodedMessage = Json.encodeToString(decodedMessage)
            chatMembers
                .filter { it.member.uid != decodedMessage.sender }
                .forEach { chatMember ->
                    chatMember.session.send(encodedMessage)
                }
        }
    }

    override fun onLeave(chatMember: ChatMember) {
        chatMembers.remove(chatMember)
    }
}
