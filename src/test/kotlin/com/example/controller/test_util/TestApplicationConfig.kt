package com.example.controller.test_util

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*


fun testApp(
    startAppBeforeClientCall: Boolean = true,
    block: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit
) = testApplication {
    environment {
        config = ApplicationConfig("application-test.conf")
    }
    val client = createClient {
        install(ContentNegotiation) {
            json()
        }
    }
    if (startAppBeforeClientCall) {
        startApplication()
    }
    block(client)
}

fun testAppSocket(
    startAppBeforeClientCall: Boolean = true,
    block: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit
) = testApplication {
    environment {
        config = ApplicationConfig("application-test.conf")
    }
    val client = createClient {
        install(ContentNegotiation) {
            json()
        }
        install(WebSockets)
    }
    if (startAppBeforeClientCall) {
        startApplication()
    }
    block(client)
}

