package tech.witkor.services.web

import io.ktor.server.application.*
import kotlinx.coroutines.launch
import org.koin.ktor.plugin.Koin
import tech.witkor.services.web.coroutines.Coroutine
import tech.witkor.services.web.coroutines.RefreshServersCoroutine
import tech.witkor.services.web.plugins.*
import kotlin.time.Duration.Companion.minutes

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Koin){
        modules(configureDatabases())
        modules(configureHttpClient())
        modules(configureAuthentication())
    }

    configureSerialization()
    configureRouting()

    launch {
        listOf<Coroutine>(
            RefreshServersCoroutine()
        ).forEach { it.execute() }
    }
}
