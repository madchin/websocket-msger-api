package com.example.controller.feature_sign_in_up

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.controller.util.ErrorResponse
import com.example.controller.util.ErrorType
import com.example.data.model.User
import com.example.data.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import java.util.*


fun Route.signInUp(userService: UserService) {
    val jwtAudience = environment?.config?.property("jwt.audience")?.getString()
    val jwtDomain = environment?.config?.property("jwt.domain")?.getString()
    val jwtSecret = environment?.config?.property("jwt.secret")?.getString()

    post("/sign-in") {
        val user = call.receive<User>()

        val token = JWT.create()
            .withAudience(jwtAudience)
            .withIssuer(jwtDomain)
            .withClaim("username", user.username)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(Algorithm.HMAC256(jwtSecret))

        call.respond(hashMapOf("token" to token))
    }

    authenticate("auth-oauth-google") {
        get("login") {
            call.respondRedirect("/callback")
        }

        get("/callback") {
            val principal: OAuthAccessTokenResponse.OAuth2? = call.authentication.principal()
            call.sessions.set(UserSession(principal?.accessToken.toString()))
            call.respondRedirect("/hello")
        }
    }

    post("/sign-up") {
        val user = call.receive<User>()
        val username = user.username
        val password = user.password

        if(username.isBlank()) {
            call.respond(status = HttpStatusCode.BadRequest, message = ErrorResponse(ErrorType.VALIDATION.name, "username cannot be blank"))
        }
        if(password.isBlank()) {
            call.respond(status = HttpStatusCode.BadRequest, message = ErrorResponse(ErrorType.VALIDATION.name,"password cannot be blank"))
        }
        userService.createUser(username, password)
        call.respond(HttpStatusCode.Created)
    }

}