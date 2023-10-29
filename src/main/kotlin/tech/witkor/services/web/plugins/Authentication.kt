package tech.witkor.services.web.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.core.module.Module
import org.koin.ktor.ext.inject
import tech.witkor.services.web.entities.ErrorEntity
import tech.witkor.services.web.services.AuthService

fun Application.configureAuthentication(): Module {
    fun property(property: String): String {
        return environment.config.propertyOrNull("ktor.jwt.$property")?.getString() ?: ""
    }

    install(Authentication) {
        jwt("auth-jwt") {
            realm = property("realm")
            verifier(JWT
                .require(Algorithm.HMAC256(property("secret")))
                .build()
            )
            validate {
                if (it.payload.getClaim("email").asString() != "") {
                    JWTPrincipal(it.payload)
                } else null
            }
            challenge { defaultScheme, realm ->
                ErrorEntity(401, "Token is not valid or has expired").show(call)
            }
        }
    }

    val authService = AuthService(environment.config)

    return org.koin.dsl.module {
        single<AuthService> { authService }
    }
}