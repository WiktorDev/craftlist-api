package tech.witkor.services.web.utilities

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun extractEmail(call: ApplicationCall): String {
    val principal = call.principal<JWTPrincipal>()
    return principal!!.payload.getClaim("email").asString()
}