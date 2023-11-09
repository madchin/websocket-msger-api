package com.example

import com.example.dao.DatabaseFactory
import com.example.dao.RepositoryFactory
import com.example.service.ServiceFactory
import org.junit.jupiter.api.BeforeAll
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

open class TestConfig {

    @BeforeTest
    fun init() = DatabaseFactory.createTables()

    @AfterTest
    fun tearDown() = DatabaseFactory.dropTables()

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
