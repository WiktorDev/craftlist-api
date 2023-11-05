package tech.witkor.services.web.routing.servers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import tech.witkor.services.web.entities.ErrorEntity
import tech.witkor.services.web.repositories.ServerRepository
import tech.witkor.services.web.repositories.UserRepository
import tech.witkor.services.web.routing.servers.dto.CreateServerDto
import tech.witkor.services.web.services.ServerService
import tech.witkor.services.web.utilities.extractEmail
import tech.witkor.services.web.utilities.pagination.createPagination
import tech.witkor.services.web.utilities.pagination.paginationParams
import java.util.UUID

fun Route.serversRouting() {
    val service by inject<ServerService>()

    route("/servers") {
        get {
            val pagination = paginationParams(call)
//            call.respond(
//                createPagination(repository.fetchPagedServers(pagination), pagination)
//            )
        }
        post {
            val dto = call.receive<CreateServerDto>().validate();
            service.create(dto)
            context.respond(HttpStatusCode.Created)
        }
        authenticate("auth-jwt") {
//            post {
//                val user = userRepository.findByEmail(extractEmail(call));
//                if (user == null) {
//                    ErrorEntity(403, "Forbidden")
//                    return@post
//                }
//                val dto = call.receive<CreateServerDto>().validate();
//                val result = repository.create(dto)
//                if (!result) throw BadRequestException("Serwer z takim adresem juz istnieje!")
//                context.respond(HttpStatusCode.Created)
//            }
            patch("/{hostname}/take-over") {
                val verifyId = "craftlist.pl#${UUID.randomUUID()}"
                call.respond(verifyId)
            }
        }
    }
}