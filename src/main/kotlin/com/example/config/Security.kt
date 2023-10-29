package com.example.config

import com.example.controller.util.JwtConfig
import com.example.util.ExplicitException
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun AuthenticationConfig.configureOAuth() {
    oauth("auth-oauth-google") {
        urlProvider = { "http://localhost:8080/callback" }
        providerLookup = {
            OAuthServerSettings.OAuth2ServerSettings(
                name = "google",
                authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                requestMethod = HttpMethod.Post,
                clientId = System.getenv("GOOGLE_CLIENT_ID"),
                clientSecret = System.getenv("GOOGLE_CLIENT_SECRET"),
                defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile")
            )
        }
        client = HttpClient(Apache)
    }
}

fun AuthenticationConfig.configureJWT() {
    jwt("auth-jwt") {
        verifier(JwtConfig.tokenVerifier())
        validate { JwtConfig.payloadValidator(it) }
        // Delegate response handler to StatusPages plugin
        challenge { _, _ -> throw ExplicitException.Unauthorized }
    }
}

fun Application.configureSecurity() {
    install(Authentication) {
        configureOAuth()
        configureJWT()
    }
}
