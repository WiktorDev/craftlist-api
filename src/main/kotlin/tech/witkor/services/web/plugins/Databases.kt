package tech.witkor.services.web.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
import org.koin.core.module.Module
import tech.witkor.services.web.repositories.ServerRepository

fun Application.configureDatabases(): Module {
    fun property(property: String): String {
        return environment.config.propertyOrNull("ktor.database.$property")?.getString() ?: ""
    }

    val database = Database.connect(
        url = "jdbc:mariadb://${property("hostname")}:${property("port")}/${property("database")}",
        user = property("username"),
        driver = "org.mariadb.jdbc.Driver",
        password = property("password")
    )

    val serverRepository = ServerRepository(database)

    return org.koin.dsl.module {
        single<ServerRepository> { serverRepository }
    }
}
