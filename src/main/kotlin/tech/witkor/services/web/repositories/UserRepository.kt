package tech.witkor.services.web.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import tech.witkor.services.web.utilities.ServerMode

@Serializable
data class ExposedUser(
    val id: Int,
    val email: String,
    val username: String
) {
    companion object {
        fun fromRow(row: ResultRow): ExposedUser {
            return ExposedUser(
                row[UserRepository.User.id].value,
                row[UserRepository.User.email],
                row[UserRepository.User.username]
            )
        }
    }
}

class UserRepository(database: Database) {
    object User : IntIdTable("users") {
        val email = varchar("email", 64)
        val username = varchar("username", 64)
    }

    init {
        transaction(database) {
            SchemaUtils.create(User)
            SchemaUtils.createMissingTablesAndColumns(User)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun findByEmail(email: String): ExposedUser? {
        return dbQuery {
            User.select { User.email eq email }
                .map { ExposedUser.fromRow(it) }
                .singleOrNull()
        }
    }
}