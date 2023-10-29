package tech.witkor.services.web.plugins

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.requestvalidation.*
import kotlinx.serialization.json.Json

fun Application.configureSerialization() {
    install(RequestValidation)
    install(ContentNegotiation) {
        headers {
            append("Content-Type", "application/json")
        }
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
}
