package tech.witkor.services.web.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import tech.witkor.services.web.entities.ErrorEntity
import tech.witkor.services.web.repositories.UserRepository
import tech.witkor.services.web.services.AuthService

fun Route.authRouting() {
    val authService by inject<AuthService>()
    val userRepository by inject<UserRepository>()

    route("/auth") {
        authenticate("auth-jwt") {
            get("/me") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("email").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
            }
        }
        get("/login") {
            call.respondRedirect(authService.getLoginUrl())
        }
        get("/callback") {
            val token = authService.getToken(call.request.queryParameters["code"]?:"")
            if (token == null) {
                ErrorEntity(498, "Code expired!").show(call)
                return@get
            }
            val profile = authService.getProfile(token.accessToken)
            if (profile == null) {
                ErrorEntity(498, "Token expired!").show(call)
                return@get
            }
            val user = userRepository.createIfNotExists(profile)
            val tokenPair = authService.createToken(user)
            call.respond(HttpStatusCode.OK, tokenPair)
        }
    }
}