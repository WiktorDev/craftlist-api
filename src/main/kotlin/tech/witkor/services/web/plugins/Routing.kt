package tech.witkor.services.web.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.routing.*
import kotlinx.serialization.ExperimentalSerializationApi
import tech.witkor.services.web.entities.ErrorEntity
import tech.witkor.services.web.entities.ErrorsEntity
import tech.witkor.services.web.entities.exceptions.ConflictException
import tech.witkor.services.web.entities.exceptions.ValidationException
import tech.witkor.services.web.routing.authRouting
import tech.witkor.services.web.routing.configRouting
import tech.witkor.services.web.routing.servers.serversRouting

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
            when (cause) {
                is BadRequestException -> ErrorEntity(400, cause.message).show(call)
                is NotFoundException -> ErrorEntity(404, cause.message).show(call)
                is ConflictException -> ErrorEntity(409, cause.message).show(call)
                is ValidationException -> {
                    val output = cause.message!!.replaceFirst("{", "").replace("}", "")
                        .split(",")
                        .map { it.split("=") }
                        .associate { it.first().replace(" ", "") to it.last() }
                    ErrorsEntity(400, output).show(call)
                }
                is NumberFormatException -> ErrorEntity(400, "Invalid number format! ${cause.message}").show(call)
                else -> {
                    ErrorEntity(500, "500: $cause").show(call)
                    cause.printStackTrace()
                }
            }
        }
        status(HttpStatusCode.Forbidden) { call, status ->
            ErrorEntity(403, "Forbidden resource").show(call)
        }
    }
}
