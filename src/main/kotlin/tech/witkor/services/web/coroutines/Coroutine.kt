package tech.witkor.services.web.coroutines

import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import kotlin.time.Duration

abstract class Coroutine(private val duration: Duration) : KoinComponent {
    suspend fun execute() {
        while (true) {
            this.task()
            delay(this.duration)
        }
    }
    abstract suspend fun task()
}