package tech.witkor.services.web.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import tech.witkor.services.web.routing.dto.CreateServerDto
import tech.witkor.services.web.utilities.ServerMode
import tech.witkor.services.web.utilities.pagination.PaginationDto
import java.lang.Exception
import java.sql.SQLException
import java.time.LocalDateTime
import kotlin.math.max

@Serializable
data class ExposedServer(
    val id: Int,
    val followers: Int,
    val address: String,
    val modes: List<ServerMode>,
    val versions: List<String>,
    val info: ExposedServerInfo?
) {
    companion object {
        fun fromRow(row: ResultRow): ExposedServer {
            return ExposedServer(
                row[ServerRepository.Server.id].value,
                row[ServerRepository.Server.followers],
                row[ServerRepository.Server.address],
                row[ServerRepository.Server.modes].split("||").map { ServerMode.valueOf(it) },
                row[ServerRepository.Server.versions].split("||"),
                ExposedServerInfo.fromRow(row)
            )
        }
    }
}

@Serializable
data class ExposedServerInfo(
    val id: Int,
    val onlinePlayers: Int,
    val maxPlayers: Int
) {
    companion object {
        fun fromRow(row: ResultRow): ExposedServerInfo? {
            return try {
                ExposedServerInfo(
                    row[ServerRepository.ServerInfo.id].value,
                    row[ServerRepository.ServerInfo.onlinePlayers],
                    row[ServerRepository.ServerInfo.maxPlayers]
                )
            } catch (e: Exception) { null }
        }
    }
}

class ServerRepository(database: Database) {
    object Server : IntIdTable("servers") {
        val followers = integer("followers").clientDefault { 0 }
        val address = varchar("address", 64).uniqueIndex()
        val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
        val modes = varchar("modes", 255)
        val versions = varchar("versions", 255)
        val info = reference("server_info", ServerInfo, onDelete = ReferenceOption.CASCADE)
        val userId = (integer("user_id").references(UserRepository.User.id)).index()
    }

    object ServerInfo:IntIdTable("servers_info") {
        val onlinePlayers = integer("online_players").clientDefault { 0 }
        val maxPlayers = integer("max_players").clientDefault { 0 }
    }

    init {
        transaction(database) {
            SchemaUtils.create(Server)
            SchemaUtils.createMissingTablesAndColumns(Server)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(server: CreateServerDto, user: ExposedUser): Int = dbQuery {
        try {
            if (this.isExists(server.address)) {
                return@dbQuery 0
            }
            Server.insertIgnore {
                it[followers] = 0
                it[address] = server.address
                it[modes] = server.modes.joinToString("||")
                it[versions] = server.versions.joinToString("||")
                it[info] = ServerInfo.insertAndGetId {  }
                it[userId] = user.id
            }.insertedCount
        } catch (ex: SQLException) { 0 }
    }
    suspend fun findByAddress(address: String): ExposedServer? {
        return dbQuery {
            Server.select { Server.address eq address }
                .map {
                    ExposedServer.fromRow(it)
                }
                .singleOrNull()
        }
    }
    private suspend fun isExists(address: String): Boolean {
        return findByAddress(address) != null
    }
    suspend fun findAll(): List<ExposedServer> {
        return dbQuery {
            Server.selectAll().map {
                ExposedServer.fromRow(it)
            }
        }
    }
    suspend fun fetchPagedServers(pagination: PaginationDto): List<ExposedServer> {
        return dbQuery {
            Server.leftJoin(ServerInfo).selectAll().limit(pagination.size, pagination.skip()).map {
                ExposedServer.fromRow(it)
            }
        }
    }
    suspend fun updateServerInfo(id: Int, serverInfo: ExposedServerInfo) {
        return dbQuery {
            ServerInfo.update({ ServerInfo.id eq id }) {
                it[onlinePlayers] = serverInfo.onlinePlayers
                it[maxPlayers] = serverInfo.maxPlayers
            }
        }
    }
}