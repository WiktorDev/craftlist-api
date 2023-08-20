package tech.witkor.services.web.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import tech.witkor.services.web.entities.ErrorEntity
import tech.witkor.services.web.routing.authRouting
import tech.witkor.services.web.routing.serversRouting

fun Application.configureRouting() {
    routing {
        route("api") {
            serversRouting()
            authRouting()
        }
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            if (cause is BadRequestException) {
                ErrorEntity(400, cause.message).show(call)
            }
            ErrorEntity(500, "500: $cause").show(call)
        }
    }
}
