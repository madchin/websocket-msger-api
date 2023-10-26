package com.example.socket

import com.example.service.ChatService

object SocketFactory {
    lateinit var chatRoomHandler: ChatRoomSocketHandler

    fun init(chatService: ChatService) {
        chatRoomHandler = ChatRoomSocketHandlerImpl(chatService)
    }
}