package com.example.controller.feature_sign_in_up

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.controller.util.UserSession
import com.example.domain.dao.service.UserService
import com.example.domain.model.UserDTO
import com.example.util.WrongCredentialsException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.mindrot.jbcrypt.BCrypt
import java.util.*


fun Route.signInUp(userService: UserService) {
    val jwtAudience = environment?.config?.property("jwt.audience")?.getString()
    val jwtDomain = environment?.config?.property("jwt.domain")?.getString()
    val jwtSecret = environment?.config?.property("jwt.secret")?.getString()

    post("/sign-in") {
        val receivedUser = call.receive<UserDTO>()
        userService.getUser(receivedUser.toUser()).also { user ->
            val isPasswordCorrect = BCrypt.checkpw(receivedUser.password, user.password)
            if (!isPasswordCorrect) {
                throw WrongCredentialsException
            }

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
        val userDTO = call.receive<UserDTO>()
        val hashedPassword = BCrypt.hashpw(userDTO.password, BCrypt.gensalt())
        val hashedUserDTO = userDTO.copy(password = hashedPassword)
        userService.createUser(hashedUserDTO.toUser()).also {
            call.respond(HttpStatusCode.Created)
        }

    }

}
