package com.example.controller.util

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.model.User
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*
import java.util.*

private object JwtPayload {
    const val audience = "jwt.audience"
    const val domain = "jwt.domain"
    const val secret = "jwt.secret"
    const val realm = "jwt.realm"
}

private class JwtConfigException(missingField: String) :
    Throwable(message = "Missing $missingField in application.conf")

object JwtConfig {
    private const val sevenDaysInMs = 60000L * 60000 * 24 * 7
    private lateinit var jwtAudience: String
    private lateinit var jwtDomain: String
    private lateinit var jwtSecret: String
    private lateinit var jwtRealm: String

    fun init(environment: ApplicationEnvironment?) {
        jwtAudience = environment?.config?.propertyOrNull(JwtPayload.audience)?.getString() ?: throw JwtConfigException(
            JwtPayload.audience
        )
        jwtDomain = environment.config.propertyOrNull(JwtPayload.domain)?.getString() ?: throw JwtConfigException(
            JwtPayload.domain
        )
        jwtSecret = environment.config.propertyOrNull(JwtPayload.secret)?.getString() ?: throw JwtConfigException(
            JwtPayload.secret
        )
        jwtRealm = environment.config.propertyOrNull(JwtPayload.realm)?.getString() ?: throw JwtConfigException(
            JwtPayload.realm
        )
    }

    fun createToken(user: User, expirationTime: Long = sevenDaysInMs): String = JWT.create()
        .withAudience(jwtAudience)
        .withIssuer(jwtDomain)
        .withClaim("uid", user.id)
        .withExpiresAt(Date(System.currentTimeMillis() + expirationTime))
        .sign(Algorithm.HMAC256(jwtSecret))

    fun tokenVerifier(): JWTVerifier = JWT
        .require(Algorithm.HMAC256(jwtSecret))
        .withAudience(jwtAudience)
        .withIssuer(jwtDomain)
        .build()

    fun payloadValidator(credential: JWTCredential): JWTPrincipal? {
        val payload = credential.payload
        val containsAudience = payload.audience.contains(jwtAudience)
        val userId = payload.getClaim("uid").asString()
        val isUserIdProper = userId != null && userId.isNotBlank()
        return if (containsAudience && isUserIdProper) {
            JWTPrincipal(payload)
        } else {
            null
        }
    }

}