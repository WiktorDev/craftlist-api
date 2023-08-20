package tech.witkor.services.web.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class ExposedUser(
    val email: String,
    val username: String
) {}

class UserRepository(database: Database) {
    object User : IntIdTable("users") {
        val email = varchar("email", 64)
        val username = varchar("username", 64)
    }

    init {
        transaction(database) {
            SchemaUtils.create(User)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}