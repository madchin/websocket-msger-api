package com.example.service

import com.example.TestConfig
import com.example.model.UserDTO
import com.example.util.ExplicitException
import com.example.util.PasswordHasher
import kotlinx.coroutines.runBlocking
import kotlin.test.*


class AuthServiceTest : TestConfig() {
    @Test
    fun `Fail to login when user with username not exists`(): Unit = runBlocking {
        assertFailsWith<ExplicitException.UserNotFound> {
            ServiceFactory.authService.login(wrongEmailUser)
        }
    }

    @Test
    fun `Fail to login when password is wrong`(): Unit = runBlocking {
        ServiceFactory.authService.register(firstUser)
        assertFailsWith<ExplicitException.WrongCredentials> {
            ServiceFactory.authService.login(wrongPasswordUser)
        }
    }

    @Test
    fun `Successfully login`(): Unit = runBlocking {
        ServiceFactory.authService.register(firstUser)
        val loggedUser = ServiceFactory.authService.login(firstUser)
        assertEquals(firstUser.email, loggedUser.email)
        assertTrue { PasswordHasher.checkPassword(firstUser.password, loggedUser.password) }

    }

    @Test
    fun `Fail to register when user is not unique`(): Unit = runBlocking {
        val user = ServiceFactory.authService.register(firstUser)

        assertFailsWith<ExplicitException.DuplicateUser> {
            ServiceFactory.authService.register(user.toUserDTO())
        }
    }

    @Test
    fun `Successfully register`(): Unit = runBlocking {
        val user = ServiceFactory.authService.register(firstUser)
        assertEquals(firstUser.email, user.email)
        assertTrue { PasswordHasher.checkPassword(firstUser.password, user.password) }
        assertNotNull(user.id)
    }

    private companion object {
        val firstUser = UserDTO("email", "password")
        val wrongEmailUser = firstUser.copy("anotherusername@easd.com")
        val wrongPasswordUser = firstUser.copy(password = "xppasddas")
    }
}