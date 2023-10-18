package com.example.plugins

import com.example.controller.util.*
import com.example.util.ForbiddenException
import com.example.util.GenericException
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.forwardedheaders.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.*

fun Application.configureHTTP() {
    val secretEncryptKey = hex(environment.config.property("ktor.security.session.encryptKey").getString())
    val secretSignKey = hex(environment.config.property("ktor.security.session.signKey").getString())
    routing {
        swaggerUI(path = "openapi")
    }
    install(ForwardedHeaders) // WARNING: for security, do not include this if not behind a reverse proxy
    install(XForwardedHeaders) // WARNING: for security, do not include this if not behind a reverse proxy
    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }
    install(Sessions) {
        header<UserSession>("user_session") {
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretSignKey))
        }
    }
    install(RequestValidation) {
        validateUser()
        validateChat()
        validateMember()
        validateMessage()
    }
    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.reasons.joinToString())
        }
        exception<Throwable> { call, cause ->
            when (cause) {
                is NotFoundException -> call.respond(HttpStatusCode.NotFound, cause.message.toString())
                is BadRequestException -> call.respond(HttpStatusCode.BadRequest, cause.message.toString())
                is ForbiddenException -> call.respond(HttpStatusCode.Forbidden)
                else -> call.respond(HttpStatusCode.BadRequest, GenericException.message.toString())
            }
        }
    }
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("user_session")
        exposeHeader("user_session")
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }
    install(CachingHeaders) {
        options { call, outgoingContent ->
            when (outgoingContent.contentType?.withoutParameters()) {
                ContentType.Text.CSS -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 24 * 60 * 60))
                else -> null
            }
        }
    }
}
