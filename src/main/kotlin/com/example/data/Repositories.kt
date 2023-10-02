package com.example.data

import com.example.data.repository.*

interface Repositories {
    val chatRepository: ChatRepository
    val userRepository: UserRepository
//    val memberRepository: MemberRepository
//    val messageRepository: MessageRepository
}