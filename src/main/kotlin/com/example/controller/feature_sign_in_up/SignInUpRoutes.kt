package com.example.controller.feature_sign_in_up

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.controller.util.ErrorResponse
import com.example.controller.util.ErrorType
import com.example.domain.model.User
import com.example.domain.dao.service.UserService
import com.example.data.util.GenericException
import com.example.data.util.UserSession
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
        val receivedUser = call.receive<User>()

        userService.getUser(receivedUser).also { result ->
            result.onSuccess {
                val sevenDaysInMs = 60000L * 60000 * 24 * 7
                val token = JWT.create()
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .withClaim("username", it.username)
                    .withExpiresAt(Date(System.currentTimeMillis() + sevenDaysInMs))
                    .sign(Algorithm.HMAC256(jwtSecret))

                call.sessions.set(UserSession(uid = it.id ?: ""))
                call.respond(hashMapOf("token" to token, "uid" to it.id))
            }
            result.onFailure {
                val message = result.exceptionOrNull()?.message ?: ""
                val type =
                    if (message != GenericException().message) ErrorType.NOT_FOUND.name else ErrorType.GENERIC.name
                val statusCode =
                    if (type == ErrorType.NOT_FOUND.name) HttpStatusCode.NotFound else HttpStatusCode.BadRequest
                call.respond(
                    statusCode,
                    ErrorResponse(type = type, message = message)
                )
            }
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
    val receivedUser = call.receive<User>()

    receivedUser.let { user ->

        userService.createUser(User(username = user.username, password = user.password, email = "temp")).also { result ->
            result.onSuccess {
                call.respond(HttpStatusCode.Created)
            }

            result.onFailure {
                val message = result.exceptionOrNull()?.message ?: ""
                val type = when (message) {
                    GenericException().message -> ErrorType.GENERIC.name
                    "User with ${user.username} already exists" -> ErrorType.ALREADY_EXISTS.name
                    else -> ErrorType.NOT_FOUND.name
                }
                val statusCode =
                    if (type == ErrorType.NOT_FOUND.name) HttpStatusCode.NotFound else HttpStatusCode.BadRequest
                call.respond(
                    statusCode,
                    ErrorResponse(type = type, message = message)
                )
            }
        }
    }
}

}
