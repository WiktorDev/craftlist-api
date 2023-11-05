package tech.witkor.services.web.coroutines

import org.koin.core.component.inject
import tech.witkor.services.web.repositories.ServerRepository
import kotlin.time.Duration.Companion.minutes

class RefreshServersCoroutine : Coroutine(1.minutes) {
    private val serverRepository: ServerRepository by inject()

    override suspend fun task() {
//        this.serverRepository.findAll().forEach {
//            val id = it.info?.id ?: it.id
//            // Tu bedzie pobieranie info o serwerze
//            this.serverRepository.updateServerInfo(id, ExposedServerInfo(id, 2, 6))
//        }
    }
}