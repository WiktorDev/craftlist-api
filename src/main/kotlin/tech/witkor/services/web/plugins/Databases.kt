package tech.witkor.services.web.plugins

import io.ktor.server.application.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.*
import org.koin.core.module.Module
import tech.witkor.services.web.repositories.ServerRepository
import tech.witkor.services.web.repositories.UserRepository

fun Application.configureDatabases(): Module {
    fun property(property: String): String {
        return environment.config.propertyOrNull("ktor.database.$property")?.getString() ?: ""
    }

    val dataSourceUri = "jdbc:mariadb://${property("hostname")}:${property("port")}/${property("database")}"

    val flyway = Flyway.configure()
        .dataSource(dataSourceUri, property("username"), property("password"))
        .locations("database/migrations")
        .load()
    flyway.migrate().migrations.forEach {
        println(it.filepath)
    }

    val database = Database.connect(
        url = dataSourceUri,
        user = property("username"),
        driver = "org.mariadb.jdbc.Driver",
        password = property("password")
    )

    val serverRepository = ServerRepository(database)
    val userRepository = UserRepository(database)

    return org.koin.dsl.module {
        single<ServerRepository> { serverRepository }
        single<UserRepository> { userRepository }
    }
}
