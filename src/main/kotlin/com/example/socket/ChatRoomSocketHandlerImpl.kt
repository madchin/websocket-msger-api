package com.example.socket

import com.example.model.ChatMember
import com.example.model.MessageDTO
import com.example.service.ChatService
import com.example.service.MemberService
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class ChatRoomSocketHandlerImpl(
    private val chatService: ChatService,
    private val memberService: MemberService
) : ChatRoomSocketHandler {
    override val chatMembers: MutableSet<ChatMember> = Collections.synchronizedSet(LinkedHashSet())
    override suspend fun onJoin(memberSession: DefaultWebSocketSession, userId: String, chatId: String) {
        val chat = runCatching {
            chatService.findChat(chatId)
        }.getOrNull()
        val member = runCatching {
            memberService.getMember(userId)
        }.getOrNull()
        if (chat == null) {
            memberSession.close(CloseReason(CloseReason.Codes.NORMAL, CHAT_NOT_FOUND_MESSAGE))
            return
        }
        if (member == null) {
            memberSession.close(CloseReason(CloseReason.Codes.NORMAL, MEMBER_NOT_FOUND_MESSAGE))
            return
        }
        val chatMember = ChatMember(memberSession, member)
        chatMembers.add(chatMember)
        val message = Json.encodeToString(MessageDTO(member.uid, "Member ${member.name} joined chat"))
        memberSession.send(message)
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

    override fun onLeave(memberSession: DefaultWebSocketSession, userId: String) {
        chatMembers.removeIf { it.member.uid == userId }
    }

    companion object {
        const val CHAT_NOT_FOUND_MESSAGE = "Chat not found"
        const val MEMBER_NOT_FOUND_MESSAGE = "Member not found"
    }
}
