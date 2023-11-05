package tech.witkor.services.web.services

import io.ktor.client.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tech.witkor.services.web.entities.exceptions.ConflictException
import tech.witkor.services.web.repositories.ServerRepository
import tech.witkor.services.web.routing.servers.dto.CreateServerDto

class ServerService() : KoinComponent {
    private val serverRepository: ServerRepository by inject()

    fun create(dto: CreateServerDto) {
        println("e")
        val state = this.serverRepository.create(dto)
        if (!state) throw ConflictException("This server already exists!")
    }
}