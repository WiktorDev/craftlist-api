package tech.witkor.services.web.services

import io.ktor.client.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tech.witkor.services.web.entities.exceptions.ConflictException
import tech.witkor.services.web.models.*
import tech.witkor.services.web.repositories.ServerRepository
import tech.witkor.services.web.routing.servers.dto.CreateServerDto
import tech.witkor.services.web.utilities.pagination.PaginationDto

class ServerService() : KoinComponent {
    private val serverRepository: ServerRepository by inject()

    fun create(dto: CreateServerDto) {
        val state = this.serverRepository.create(dto)
//        if (state) throw ConflictException("This server already exists!")
    }
    fun fetchPagedServers(pagination: PaginationDto): List<ExposedServer> = transaction {
        (Servers innerJoin ServersInfo).selectAll().limit(pagination.size, pagination.skip()).map {
            exposeServer(Server.wrapRow(it))
        }
//        Servers.leftJoin(ServersInfo).selectAll().limit(pagination.size, pagination.skip()).map {
//            exposeServer(Server.wrapRow(it))
//        }
    }
}