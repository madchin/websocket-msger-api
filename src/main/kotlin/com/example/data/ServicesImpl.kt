package com.example.data

import com.example.data.service.ChatServiceImpl
import com.example.data.service.MemberServiceImpl
import com.example.data.service.MessageServiceImpl
import com.example.data.service.UserServiceImpl

class ServicesImpl(repositories: RepositoriesImpl) : Services {
    override val chatService = ChatServiceImpl(repositories.chatRepository)
    override val userService = UserServiceImpl(repositories.userRepository)
    override val messageService = MessageServiceImpl(repositories.messageRepository)
    override val memberService = MemberServiceImpl(repositories.memberRepository)
}