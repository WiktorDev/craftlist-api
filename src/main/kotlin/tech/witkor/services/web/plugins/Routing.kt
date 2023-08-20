package tech.witkor.services.web.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import tech.witkor.services.web.entities.ErrorEntity
import tech.witkor.services.web.routing.serversRouting

fun Application.configureRouting() {
    routing {
        route("api") {
            serversRouting()
        }
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            if (cause is BadRequestException) {
                call.response.status(HttpStatusCode.BadRequest)
                call.respond(ErrorEntity(400, cause.message))
            }
            call.response.status(HttpStatusCode.InternalServerError)
            call.respond(ErrorEntity(500, "500: $cause"))
        }
    }
}
