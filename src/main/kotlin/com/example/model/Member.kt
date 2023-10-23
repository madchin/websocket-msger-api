package com.example.model

import io.ktor.websocket.*
import kotlinx.serialization.Serializable

class ChatMember(val session: DefaultWebSocketSession, val member: Member)

@Serializable
data class Member(val uid: String, val name: String) {
    fun toChatMember(session: DefaultWebSocketSession) = ChatMember(session, this)
}


@Serializable
data class MemberDTO(val name: String) {
    fun toMember(uid: String) = Member(uid, name)
}