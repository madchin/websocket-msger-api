package com.example.domain.dao

import com.example.domain.dao.repository.ChatRepository
import com.example.domain.dao.repository.MemberRepository
import com.example.domain.dao.repository.MessageRepository
import com.example.domain.dao.repository.UserRepository

interface Repositories {
    val chatRepository: ChatRepository
    val userRepository: UserRepository
    val memberRepository: MemberRepository
    val messageRepository: MessageRepository
}