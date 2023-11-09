package com.example.service

import com.example.TestConfig
import com.example.model.ChatDTO
import com.example.model.MessageDTO
import com.example.util.ExplicitException
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.test.*

class ChatServiceTest : TestConfig() {
    @Test
    fun `Successfully create chat`(): Unit = runBlocking {
        val timestamp = System.currentTimeMillis()
        val createdChat = ServiceFactory.chatService.createChat(chat, FIRST_USER_ID, timestamp)

        assertEquals(chat.name, createdChat.name)
        assertContains(createdChat.lastSeenMembers, mapOf(FIRST_USER_ID to timestamp))
    }

    @Test
    fun `Fail to get chat which not exists`(): Unit = runBlocking {
        assertFailsWith<ExplicitException.ChatNotFound> {
            ServiceFactory.chatService.getChat(chatId, FIRST_USER_ID)
        }
    }

    @Test
    fun `Fail to get chat which user is not member`(): Unit = runBlocking {
        val createdChat = ServiceFactory.chatService.createChat(chat, FIRST_USER_ID)
        assertFailsWith<ExplicitException.Forbidden> {
            ServiceFactory.chatService.getChat(createdChat.id!!, SECOND_USER_ID)
        }
    }

    @Test
    fun `Successfully get chat`(): Unit = runBlocking {
        val createdChat = ServiceFactory.chatService.createChat(chat, FIRST_USER_ID)
        val readChat = ServiceFactory.chatService.getChat(createdChat.id!!, FIRST_USER_ID)

        assertNotNull(readChat.id)
        assertEquals(createdChat.id, readChat.id)
        assertEquals(createdChat.lastSeenMembers, readChat.lastSeenMembers)
        assertEquals(createdChat.name, readChat.name)
        assertEquals(createdChat.messageIds, readChat.messageIds)
    }

    @Test
    fun `Fail to find chat`(): Unit = runBlocking {
        assertFailsWith<ExplicitException.ChatNotFound> {
            ServiceFactory.chatService.getChat(chatId, FIRST_USER_ID)
        }
    }

    @Test
    fun `Successfully find chat`(): Unit = runBlocking {
        val createdChat = ServiceFactory.chatService.createChat(chat, FIRST_USER_ID)
        val readChat = ServiceFactory.chatService.findChat(createdChat.id!!)

        assertNotNull(readChat.id)
        assertEquals(createdChat.id, readChat.id)
        assertEquals(createdChat.lastSeenMembers, readChat.lastSeenMembers)
        assertEquals(createdChat.name, readChat.name)
        assertEquals(createdChat.messageIds, readChat.messageIds)
    }

    @Test
    fun `Fail to change chat name of chat which not exists`(): Unit = runBlocking {
        assertFailsWith<ExplicitException.ChatNotFound> {
            ServiceFactory.chatService.changeChatName(chatId, CHAT_NAME, FIRST_USER_ID)
        }
    }

    @Test
    fun `Fail to change chat name of chat which user is not member`(): Unit = runBlocking {
        val createdChat = ServiceFactory.chatService.createChat(chat, FIRST_USER_ID)

        assertFailsWith<ExplicitException.Forbidden> {
            ServiceFactory.chatService.changeChatName(createdChat.id!!, CHAT_NAME, SECOND_USER_ID)
        }
    }

    @Test
    fun `Successfully change chat name`(): Unit = runBlocking {
        val createdChat = ServiceFactory.chatService.createChat(chat, FIRST_USER_ID)
        val changedChat = ServiceFactory.chatService.changeChatName(createdChat.id!!, CHANGED_CHAT_NAME, FIRST_USER_ID)

        assertEquals(CHANGED_CHAT_NAME, changedChat.name)

    }

    @Test
    fun `Fail to join chat which not exists`(): Unit = runBlocking {
        assertFailsWith<ExplicitException.ChatNotFound> {
            ServiceFactory.chatService.joinChat(chatId, FIRST_USER_ID)
        }
    }

    @Test
    fun `Successfully join chat`(): Unit = runBlocking {
        val createdChat = ServiceFactory.chatService.createChat(chat, FIRST_USER_ID)
        val timestamp = System.currentTimeMillis()
        val joinedChat = ServiceFactory.chatService.joinChat(createdChat.id!!, SECOND_USER_ID, timestamp)

        assertContains(joinedChat.lastSeenMembers, mapOf(SECOND_USER_ID to timestamp))
    }

    @Test
    fun `Fail to send message when chat not exist`(): Unit = runBlocking {
        assertFailsWith<ExplicitException.ChatNotFound> {
            ServiceFactory.chatService.sendMessage(message.toMessage(chatId))
        }
    }

    @Test
    fun `Fail to send message in chat which user is not member`(): Unit = runBlocking {
        val createdChat = ServiceFactory.chatService.createChat(chat, FIRST_USER_ID)
        val parsedMessage = message.copy(sender = SECOND_USER_ID)

        assertFailsWith<ExplicitException.Forbidden> {
            ServiceFactory.chatService.sendMessage(parsedMessage.toMessage(createdChat.id!!))
        }
    }

    @Test
    fun `Successfully send message`(): Unit = runBlocking {
        val createdChat = ServiceFactory.chatService.createChat(chat, FIRST_USER_ID)
        val result = ServiceFactory.chatService.sendMessage(message.toMessage(createdChat.id!!))

        assertTrue(result)
    }

    @Test
    fun `Fail to read messages when chat not exist`(): Unit = runBlocking {
        assertFailsWith<ExplicitException.ChatNotFound> {
            ServiceFactory.chatService.readMessages(chatId, FIRST_USER_ID)
        }
    }

    @Test
    fun `Fail to read messages from chat which user is not member`(): Unit = runBlocking {
        assertFailsWith<ExplicitException.ChatNotFound> {
            ServiceFactory.chatService.readMessages(chatId, FIRST_USER_ID)
        }
    }

    @Test
    fun `Fail to read messages when messages not exist`(): Unit = runBlocking {
        val createdChat = ServiceFactory.chatService.createChat(chat, FIRST_USER_ID)
        assertFailsWith<ExplicitException.MessagesNotFound> {
            ServiceFactory.chatService.readMessages(createdChat.id!!, FIRST_USER_ID)
        }
    }

    @Test
    fun `Successfully read messages`(): Unit = runBlocking {
        val createdChat = ServiceFactory.chatService.createChat(chat, FIRST_USER_ID)
        ServiceFactory.chatService.sendMessage(message.toMessage(createdChat.id!!))
        val messages = ServiceFactory.chatService.readMessages(createdChat.id!!, FIRST_USER_ID)

        assertTrue { messages.isNotEmpty() }
    }

    private companion object {
        const val FIRST_USER_ID = "first_user_id"
        const val SECOND_USER_ID = "second_user_id"
        const val CHAT_NAME = "chat_name"
        const val CHANGED_CHAT_NAME = "changed_chat_name"
        val chatId = UUID.randomUUID().toString()
        const val CONTENT = "random message content"
        val chat = ChatDTO(CHAT_NAME)
        val message = MessageDTO(FIRST_USER_ID, CONTENT)
    }
}