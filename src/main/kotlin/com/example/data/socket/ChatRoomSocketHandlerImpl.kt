package com.example.data.socket

import com.example.domain.model.ChatMember
import com.example.domain.model.Message
import com.example.domain.socket.ChatRoomSocketHandler
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class ChatRoomSocketHandlerImpl : ChatRoomSocketHandler {
    override val chatMembers: MutableSet<ChatMember> = Collections.synchronizedSet(LinkedHashSet())
    override suspend fun onReceiveMessage(frame: Frame, saveMessage: suspend (Message) -> Result<Boolean>) {
        if (frame is Frame.Text) {
            val decodedMessage: Message = Json.decodeFromString(frame.readText())
            saveMessage(decodedMessage)
            val encodedMessage = Json.encodeToString(decodedMessage)
            try {

            chatMembers
                .filter { it.member.uid != decodedMessage.sender }
                .forEach { chatMember ->
                    chatMember.session.send(encodedMessage)
                }
            }
            catch (e: Exception) {
                println("exceptiopn is ${e.localizedMessage}")
            }
        }
    }

    override suspend fun onJoin(addChatMember: (MutableSet<ChatMember>) -> Unit) {
        addChatMember(chatMembers)
    }
}
