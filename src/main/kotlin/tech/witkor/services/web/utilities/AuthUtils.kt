package tech.witkor.services.web.utilities

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import tech.witkor.services.web.entities.exceptions.UnauthorizedException
import tech.witkor.services.web.models.User
import tech.witkor.services.web.models.Users

fun extractEmail(call: ApplicationCall): String {
    val principal = call.principal<JWTPrincipal>()
    return principal!!.payload.getClaim("email").asString()
}
fun extractUser(call: ApplicationCall): User = transaction {
    Users.select { Users.email eq extractEmail(call) }
        .map { User.wrapRow(it) }
        .singleOrNull() ?: throw UnauthorizedException("Invalid JWT token!")
}