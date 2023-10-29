package tech.witkor.services.web.plugins

import com.auth0.jwt.exceptions.TokenExpiredException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import tech.witkor.services.web.entities.ErrorEntity
import tech.witkor.services.web.routing.authRouting
import tech.witkor.services.web.routing.configRouting
import tech.witkor.services.web.routing.serversRouting

@OptIn(ExperimentalSerializationApi::class)
fun Application.configureRouting() {
    routing {
        route("api") {
            serversRouting()
            authRouting()
            configRouting()
        }
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            if (cause is BadRequestException) {
                ErrorEntity(400, cause.message).show(call)
            }
            ErrorEntity(500, "500: $cause").show(call)
            cause.printStackTrace()
        }
        status(HttpStatusCode.Forbidden) { call, status ->
            ErrorEntity(403, "Forbidden resource").show(call)
        }
    }
}
