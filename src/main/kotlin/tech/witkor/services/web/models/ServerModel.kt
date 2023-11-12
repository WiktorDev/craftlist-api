package tech.witkor.services.web.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

@Serializable
data class ExposedServer(
    val hostname: String,
    val modes: List<String>,
    val versions: List<String>,
    val followers: Int,
    val info: ExposedServerInfo
)

object Servers : IntIdTable("servers") {
    val hostname = varchar("hostname", 64).uniqueIndex()
    val port = integer("port").clientDefault { 25565 }
    val modes = varchar("modes", 255)
    val versions = varchar("versions", 255)
    val followers = integer("followers").clientDefault { 0 }
//    val verifyId = varchar("verification_id", 100).nullable()
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val serverInfo = reference("server_info_id", ServersInfo, onDelete = ReferenceOption.CASCADE)
    val user = optReference("user_id", Users)
}

class Server(id: EntityID<Int>) : IntEntity(id) {
    companion object: IntEntityClass<Server>(Servers)
    var hostname by Servers.hostname
    var port by Servers.port
    var modes by Servers.modes
    var versions by Servers.versions
    val followers by Servers.followers
    val createdAt by Servers.createdAt
    var serverInfo by ServerInfo referencedOn(Servers.serverInfo)
    var user by User optionalReferencedOn(Servers.user)
}

fun exposeServer(server: Server): ExposedServer {
    return ExposedServer(
        server.hostname,
        server.modes.split("||"),
        server.versions.split("||"),
        server.followers,
        exposeServerInfo(server.serverInfo)
    )
}