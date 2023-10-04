package com.example

import com.example.data.dao.DatabaseFactory
import com.example.plugins.*
import io.ktor.network.tls.certificates.*
import io.ktor.server.application.*
import java.io.File

fun main(args: Array<String>) {
    val keyStoreFile = File("./keystore.jks")
    val keyStore = buildKeyStore {
        certificate("sampleAlias") {
            password = "foobar"
            domains = listOf("127.0.0.1", "0.0.0.0", "localhost")
        }
    }
    keyStore.saveToFile(keyStoreFile, "foobar")
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val services = DatabaseFactory.init(false, environment = environment)
    configureSockets()
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
    configureRouting(services)
}
