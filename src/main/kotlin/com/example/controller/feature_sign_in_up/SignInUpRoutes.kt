package com.example.controller.feature_sign_in_up

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.controller.util.UserSession
import com.example.domain.model.UserDTO
import com.example.domain.service.AuthService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import java.util.*


fun Route.signInUp(authService: AuthService) {
    val jwtAudience = environment?.config?.property("jwt.audience")?.getString()
    val jwtDomain = environment?.config?.property("jwt.domain")?.getString()
    val jwtSecret = environment?.config?.property("jwt.secret")?.getString()

    post("/sign-in") {
        val userDto = call.receive<UserDTO>()
        authService.login(userDto).also { user ->
            val sevenDaysInMs = 60000L * 60000 * 24 * 7
            val token = JWT.create()
                .withAudience(jwtAudience)
                .withIssuer(jwtDomain)
                .withClaim("uid", user.id)
                .withExpiresAt(Date(System.currentTimeMillis() + sevenDaysInMs))
                .sign(Algorithm.HMAC256(jwtSecret))

            call.sessions.set(UserSession(uid = user.id!!))
            call.respond(hashMapOf("token" to token, "uid" to user.id))
        }
    }


    authenticate("auth-oauth-google") {
        get("login") {
            call.respondRedirect("/callback")
        }

        get("/callback") {
            val principal: OAuthAccessTokenResponse.OAuth2? = call.authentication.principal()
            //call.sessions.set(UserSession(principal?.accessToken.toString()))
            call.respondRedirect("/hello")
        }
    }

    post("/sign-up") {
        val userDto = call.receive<UserDTO>()
        authService.register(userDto).also {
            call.respond(HttpStatusCode.Created)
        }
    }

}
