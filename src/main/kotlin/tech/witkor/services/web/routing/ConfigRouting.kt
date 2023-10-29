package tech.witkor.services.web.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import tech.witkor.services.web.utilities.ServerMode
import tech.witkor.services.web.utilities.VersionsEnum

fun Route.configRouting() {
    route("/config") {
        get("/modes") {
            call.respond(ServerMode.mapping())
        }
        get("/versions") {
            call.respond(VersionsEnum.mapping())
        }
    }
}