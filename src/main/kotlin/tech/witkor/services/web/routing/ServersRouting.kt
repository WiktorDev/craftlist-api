package tech.witkor.services.web.routing

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
import tech.witkor.services.web.routing.dto.CreateServerDto
import tech.witkor.services.web.routing.dto.PaginationDto
import tech.witkor.services.web.utilities.extractEmail

fun Route.serversRouting() {
    val repository by inject<ServerRepository>()
    val userRepository by inject<UserRepository>()

    route("/servers") {
        get {
            val pagination = PaginationDto.from(call.request)
            call.respond(repository.fetchPagedServers(pagination))
        }
        authenticate("auth-jwt") {
            post {
                val user = userRepository.findByEmail(extractEmail(call));
                if (user == null) {
                    ErrorEntity(403, "Forbidden")
                    return@post
                }
                val dto = call.receive<CreateServerDto>().validate();
                val result = repository.create(dto, user)
                if (result == 0) throw BadRequestException("Serwer z takim adresem juz istnieje!")
                context.respond(HttpStatusCode.Created)
            }
        }
    }
}