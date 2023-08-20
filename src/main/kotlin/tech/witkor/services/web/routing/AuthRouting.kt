package tech.witkor.services.web.routing

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRouting() {
    authenticate("auth-jwt") {
        get("/me") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("email").asString()
            val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
            call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
        }
    }
    post("/login") {

    }
}