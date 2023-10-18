package com.example.data.socket

import com.example.domain.dao.service.MessageService
import com.example.domain.model.ChatMember
import com.example.domain.model.Message
import com.example.domain.socket.ChatRoomSocketHandler
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.reflect.KSuspendFunction1

class ChatRoomSocketHandlerImpl(
    private val messageService: MessageService
) : ChatRoomSocketHandler {
    override val chatMembers: MutableSet<ChatMember> = Collections.synchronizedSet(LinkedHashSet())

    override suspend fun broadcastMessage(frame: Frame) {
        if (frame is Frame.Text) {
            val decodedMessage: Message = Json.decodeFromString(frame.readText())
            messageService.saveMessage(decodedMessage)
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
