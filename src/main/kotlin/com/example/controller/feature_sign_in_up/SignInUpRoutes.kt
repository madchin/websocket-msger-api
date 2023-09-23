package com.example.controller.feature_sign_in_up

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.controller.util.ErrorResponse
import com.example.controller.util.ErrorType
import com.example.data.model.User
import com.example.data.service.UserService
import com.example.data.util.GenericException
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
        val user = try {
            call.receive<User>()
        } catch (e: ContentTransformationException) {
            null
        }
        if (user == null) call.respond(HttpStatusCode.UnprocessableEntity)
        user?.let {
            val username = it.username
            val password = it.password

            if (username.isBlank() || password.isBlank()) {
                val field = if (username.isBlank()) "username" else "password"
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse(type = ErrorType.VALIDATION.name, message = "$field cannot be blank")
                )
                return@post
            }

            val result = userService.getUser(username)
            when {
                result.isSuccess -> {
                    val token = JWT.create()
                        .withAudience(jwtAudience)
                        .withIssuer(jwtDomain)
                        .withClaim("username", it.username)
                        .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                        .sign(Algorithm.HMAC256(jwtSecret))

                    call.respond(hashMapOf("token" to token))
                }
                result.isFailure -> {
                    val message = result.exceptionOrNull()?.message ?: ""
                    val type = if(message != GenericException().message) ErrorType.NOT_FOUND.name else ErrorType.GENERIC.name
                    val statusCode = if(type == ErrorType.NOT_FOUND.name) HttpStatusCode.NotFound else HttpStatusCode.BadRequest
                    call.respond(
                        statusCode,
                        ErrorResponse(type = type, message = message)
                    )
                }
            }

        }
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
        val user = try {
            call.receive<User>()
        } catch (e: ContentTransformationException) {
            null
        }
        if (user == null) call.respond(HttpStatusCode.UnprocessableEntity)
        user?.let {
            val username = it.username
            val password = it.password

            if (username.isBlank() || password.isBlank()) {
                val field = if (username.isBlank()) "username" else "password"
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = ErrorResponse(ErrorType.VALIDATION.name, "$field cannot be blank")
                )
                return@post
            }

            val result = userService.createUser(username, password)
            when {
                result.isSuccess -> {
                    call.respond(HttpStatusCode.Created)
                }
                result.isFailure -> {
                    val message = result.exceptionOrNull()?.message ?: ""
                    val type = when (message) {
                        GenericException().message -> ErrorType.GENERIC.name
                        "User with $username already exists" -> ErrorType.NOT_FOUND.name
                        else -> ErrorType.ALREADY_EXISTS.name
                    }
                    val statusCode = if(type == ErrorType.NOT_FOUND.name) HttpStatusCode.NotFound else HttpStatusCode.BadRequest
                    call.respond(
                        statusCode,
                        ErrorResponse(type = type, message = message)
                    )
                }
            }
        }
    }

}
