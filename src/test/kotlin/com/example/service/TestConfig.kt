package com.example.service

import com.example.dao.DatabaseFactory
import com.example.dao.RepositoryFactory
import org.junit.jupiter.api.BeforeAll
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

open class TestConfig {

    @BeforeTest
    fun init(): Unit = DatabaseFactory.createTables()

    @AfterTest
    fun tearDown(): Unit = DatabaseFactory.dropTables()

    private companion object {
        @JvmStatic
        @BeforeAll
        fun initAll() {
            DatabaseFactory.init(true)
            RepositoryFactory.init()
            ServiceFactory.init(
                RepositoryFactory.chatRepository,
                RepositoryFactory.messageRepository,
                RepositoryFactory.memberRepository,
                RepositoryFactory.userRepository
            )
        }
    }
}
