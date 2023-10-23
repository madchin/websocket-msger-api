package com.example.controller.feature_sign_in_up

import com.example.controller.util.JwtConfig
import com.example.controller.util.UserSession
import com.example.model.UserDTO
import com.example.service.AuthService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*


fun Route.signInUp(authService: AuthService) {

    post("/sign-in") {
        val userDto = call.receive<UserDTO>()
        authService.login(userDto).also { user ->
            val token = JwtConfig.createToken(user.id!!)

            call.sessions.set(UserSession(uid = user.id))
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
