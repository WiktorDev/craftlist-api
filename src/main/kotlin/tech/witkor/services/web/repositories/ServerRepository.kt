package tech.witkor.services.web.repositories

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import tech.witkor.services.web.entities.exceptions.ConflictException
import tech.witkor.services.web.models.*
import tech.witkor.services.web.routing.servers.dto.CreateServerDto
import tech.witkor.services.web.utilities.pagination.PaginationDto
import java.sql.SQLException

class ServerRepository(database: Database) {
    init {
        transaction(database) {
            val tables = arrayOf(Servers, ServersInfo, Tokens)

            SchemaUtils.createMissingTablesAndColumns(*tables)
            SchemaUtils.statementsRequiredToActualizeScheme(*tables).forEach {
                println("STMT: $it")
            }
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    fun create(dto: CreateServerDto){
        try {
            transaction {
                Server.new {
                    hostname = dto.hostname
                    port = dto.port
                    modes = dto.modes.joinToString("||")
                    versions = dto.versions.joinToString("||")
                    serverInfo = ServerInfo.new {
                        onlinePlayers = 0
                        maxPlayers = 0
                    }
                }
            }
        } catch (ex: SQLException) {
            throw ConflictException("Server with this hostname already exists!")
        }
    }
    fun fetchPagedServers(pagination: PaginationDto): List<ExposedServer> = transaction {
        (Servers innerJoin ServersInfo).selectAll().limit(pagination.size, pagination.skip()).map {
            exposeServer(Server.wrapRow(it))
        }
//        Servers.leftJoin(ServersInfo).selectAll().limit(pagination.size, pagination.skip()).map {
//            exposeServer(Server.wrapRow(it))
//        }
    }
    fun findByHostname(hostname: String): Server? = transaction {
        Servers.select { Servers.hostname eq hostname }
            .map { Server.wrapRow(it) }
            .singleOrNull()
    }
//    suspend fun create(server: CreateServerDto, user: ExposedUser): Int = dbQuery {
//        try {
//            if (this.isExists(server.address)) {
//                return@dbQuery 0
//            }
//            Server.insertIgnore {
//                it[followers] = 0
//                it[address] = server.address
//                it[modes] = server.modes.joinToString("||")
//                it[versions] = server.versions.joinToString("||")
//                it[info] = ServerInfo.insertAndGetId {  }
//                it[userId] = user.id
//            }.insertedCount
//        } catch (ex: SQLException) { 0 }
//    }
//    suspend fun findByAddress(address: String): ExposedServer? {
//        return dbQuery {
//            Server.select { Server.address eq address }
//                .map {
//                    ExposedServer.fromRow(it)
//                }
//                .singleOrNull()
//        }
//    }
//    private suspend fun isExists(address: String): Boolean {
//        return findByAddress(address) != null
//    }
//    suspend fun findAll(): List<ExposedServer> {
//        return dbQuery {
//            Server.selectAll().map {
//                ExposedServer.fromRow(it)
//            }
//        }
//    }
//    suspend fun fetchPagedServers(pagination: PaginationDto): List<ExposedServer> {
//        return dbQuery {
//            Server.leftJoin(ServerInfo).selectAll().limit(pagination.size, pagination.skip()).map {
//                ExposedServer.fromRow(it)
//            }
//        }
//    }
//    suspend fun updateServerInfo(id: Int, serverInfo: ExposedServerInfo) {
//        return dbQuery {
//            ServerInfo.update({ ServerInfo.id eq id }) {
//                it[onlinePlayers] = serverInfo.onlinePlayers
//                it[maxPlayers] = serverInfo.maxPlayers
//            }
//        }
//    }
}