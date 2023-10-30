package com.example.service

import com.example.model.User
import com.example.model.UserDTO
import com.example.util.ExplicitException
import com.example.util.PasswordHasher
import kotlinx.coroutines.runBlocking
import kotlin.test.*


class AuthServiceTest : TestConfig() {
    @Test
    fun `Fail to login when user with username not exists`(): Unit = runBlocking {
        assertFailsWith<ExplicitException.UserNotFound> {
            ServiceFactory.authService.login(wrongUsernameUser)
        }
    }

    @Test
    fun `Fail to login when password is wrong`(): Unit = runBlocking {
        createUser()
        assertFailsWith<ExplicitException.WrongCredentials> {
            ServiceFactory.authService.login(wrongPasswordUser)
        }
    }

    @Test
    fun `Successfully login`(): Unit = runBlocking {
        createUser()
        val loggedUser = ServiceFactory.authService.login(firstUser)
        assertEquals(firstUser.email, loggedUser.email)
        assertEquals(firstUser.username, loggedUser.username)
        assertTrue { PasswordHasher.checkPassword(firstUser.password, loggedUser.password) }

    }

    @Test
    fun `Fail to register when user is not unique`(): Unit = runBlocking {
        val user = createUser()
        assertFailsWith<ExplicitException.DuplicateUser> {
            ServiceFactory.authService.register(user.toUserDTO())
        }
    }

    @Test
    fun `Successfully register`(): Unit = runBlocking {
        val user = ServiceFactory.authService.register(firstUser)
        assertEquals(firstUser.email, user.email)
        assertEquals(firstUser.username, user.username)
        assertTrue { PasswordHasher.checkPassword(firstUser.password, user.password) }
        assertNotNull(user.id)
    }

    private companion object {
        val firstUser = UserDTO("username", "email", "password")
        val wrongUsernameUser = firstUser.copy("anotherusername")
        val wrongPasswordUser = firstUser.copy(password = "xppasddas")
        fun createUser(): User = runBlocking {
            ServiceFactory.authService.register(firstUser)
        }
    }
}