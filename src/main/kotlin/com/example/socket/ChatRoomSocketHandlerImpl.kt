package com.example.socket

import com.example.model.ChatMember
import com.example.model.Message
import com.example.service.ChatService
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class ChatRoomSocketHandlerImpl(
    private val chatService: ChatService
) : ChatRoomSocketHandler {
    override val chatMembers: MutableSet<ChatMember> = Collections.synchronizedSet(LinkedHashSet())

    override suspend fun broadcastMessage(frame: Frame) {
        if (frame is Frame.Text) {
            val decodedMessage: Message = Json.decodeFromString(frame.readText())
            chatService.sendMessage(decodedMessage)
            val encodedMessage = Json.encodeToString(decodedMessage)
            chatMembers
                .filter { it.member.uid != decodedMessage.sender }
                .forEach { chatMember ->
                    chatMember.session.send(encodedMessage)
                }
        }
    }

    override suspend fun onReceiveMessage(session: DefaultWebSocketSession, broadcastMessage: suspend (Frame) -> Unit) {
        for (frame in session.incoming) {
            broadcastMessage(frame)
        }
    }

    override suspend fun onJoin(addChatMember: (MutableSet<ChatMember>) -> Unit) {
        addChatMember(chatMembers)
    }
}
