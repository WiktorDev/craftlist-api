package tech.witkor.services.web.routing.servers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject
import tech.witkor.services.web.entities.ErrorEntity
import tech.witkor.services.web.entities.exceptions.ConflictException
import tech.witkor.services.web.models.Users
import tech.witkor.services.web.repositories.ServerRepository
import tech.witkor.services.web.repositories.UserRepository
import tech.witkor.services.web.routing.servers.dto.CreateServerDto
import tech.witkor.services.web.routing.servers.entities.TakeOverEntity
import tech.witkor.services.web.services.ServerService
import tech.witkor.services.web.services.TokenService
import tech.witkor.services.web.utilities.TokenType
import tech.witkor.services.web.utilities.extractEmail
import tech.witkor.services.web.utilities.extractUser
import tech.witkor.services.web.utilities.isValidRecord
import tech.witkor.services.web.utilities.pagination.createPagination
import tech.witkor.services.web.utilities.pagination.paginationParams
import java.util.UUID

fun Route.serversRouting() {
    val service by inject<ServerRepository>()
    val userRepository by inject<UserRepository>()
    val tokenService by inject<TokenService>()

    route("/servers") {
        get {
            val pagination = paginationParams(call)
            call.respond(
                createPagination(service.fetchPagedServers(pagination), pagination)
            )
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
                val hostname = call.parameters["hostname"]
                val user = extractUser(call)
                val server = service.findByHostname(hostname!!) ?: throw NotFoundException("Server with this hostname not found!")
                val token = tokenService.findByUser(user, TokenType.TAKE_OVER_VERIFICATION)
                val tokenString = token?.token ?: "craftlist.pl#${UUID.randomUUID()}.${server.id}"

                if (token == null) {
                    tokenService.createToken(tokenString, TokenType.TAKE_OVER_VERIFICATION, user)
                } else if(token.user != null && token.user?.id == user.id) throw ConflictException("Ten serwer zostal juz przez ciebie przejety!")
//                transaction {
//                    if (server.verifyId == null) {
//                        server.verifyId = "craftlist.pl#${UUID.randomUUID()}"
//                    }
//                    val took = isValidRecord(server.hostname, server.verifyId!!)
//                    if (took) {
//                        val user = userRepository.findByEmail(extractEmail(call));
//                        server.user = user
//                    }
//                }
                call.respond(TakeOverEntity(tokenString, false))
            }
        }
    }
}