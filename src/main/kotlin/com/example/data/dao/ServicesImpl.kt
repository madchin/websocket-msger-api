package com.example.data.dao

import com.example.data.dao.service.*
import com.example.domain.dao.Services

class ServicesImpl(repositories: RepositoriesImpl) : Services {
    override val chatService = ChatServiceImpl(repositories.chatRepository)
    override val userService = UserServiceImpl(repositories.userRepository)
    override val messageService = MessageServiceImpl(repositories.messageRepository)
    override val memberService = MemberServiceImpl(repositories.memberRepository)
}