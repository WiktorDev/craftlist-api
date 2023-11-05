package tech.witkor.services.web.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import tech.witkor.services.web.models.User
import tech.witkor.services.web.models.Users
import tech.witkor.services.web.services.UserProfileResponse

//@Serializable
//data class ExposedUser(
//    val id: Int,
//    val email: String,
//    val username: String
//) {
//    companion object {
//        fun fromRow(row: ResultRow): ExposedUser {
//            return ExposedUser(
//                row[UserRepository.User.id].value,
//                row[UserRepository.User.email],
//                row[UserRepository.User.username]
//            )
//        }
//    }
//}

class UserRepository(database: Database) {
    init {
        transaction(database) {
            SchemaUtils.createMissingTablesAndColumns(Users)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun findByEmail(email: String): User? = dbQuery {
        Users.select { Users.email eq email }
            .map { User.wrapRow(it) }
            .singleOrNull()
    }
    fun create(email: String, name: String) : User? = transaction {
        Users.insert {
            it[Users.email] = email
            it[username] = name
        }.resultedValues!!
            .map { User.wrapRow(it) }
            .singleOrNull()
    }
    suspend fun createIfNotExists(userProfileResponse: UserProfileResponse): User {
        var user = this.findByEmail(userProfileResponse.email)
        if (user == null) {
            user = this.create(userProfileResponse.email, userProfileResponse.username)
        }
        return user!!
    }
}