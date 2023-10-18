package com.example.data.dao

import com.example.domain.service.AuthService
import com.example.domain.service.Services
import com.example.service.*

class ServicesImpl(repositories: RepositoriesImpl) : Services {
    override val chatService = ChatServiceImpl(repositories.chatRepository)
    override val userService = UserServiceImpl(repositories.userRepository)
    override val messageService = MessageServiceImpl(repositories.messageRepository)
    override val memberService = MemberServiceImpl(repositories.memberRepository)
    override val authService: AuthService = AuthServiceImpl(repositories.userRepository)
}