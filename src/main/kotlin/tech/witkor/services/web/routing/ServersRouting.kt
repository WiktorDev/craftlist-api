package tech.witkor.services.web.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import tech.witkor.services.web.repositories.ServerRepository
import tech.witkor.services.web.routing.dto.CreateServerDto
import tech.witkor.services.web.routing.dto.PaginationDto

fun Route.serversRouting() {
    val repository by inject<ServerRepository>()

    route("/servers") {
        get {
            val pagination = PaginationDto.from(call.request)
            call.respond(repository.fetchPagedServers(pagination))
        }
        post {
            val dto = call.receive<CreateServerDto>().validate();
            val result = repository.create(dto)
            if (result == 0) throw BadRequestException("Serwer z takim adresem juz istnieje!")
            context.respond(HttpStatusCode.Created)
        }
    }
}