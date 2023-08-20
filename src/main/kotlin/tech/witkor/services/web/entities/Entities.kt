package tech.witkor.services.web.entities

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

@Serializable
data class ErrorEntity(val code: Int, val message: String?) {
    suspend fun show(call: ApplicationCall) {
        call.response.status(HttpStatusCode.fromValue(this.code))
        call.respond(this)
    }
}