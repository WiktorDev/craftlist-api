package tech.witkor.services.web.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Servers : IntIdTable("servers") {
    val hostname = varchar("hostname", 64).uniqueIndex()
    val port = integer("port").clientDefault { 25565 }
    val modes = varchar("modes", 255)
    val versions = varchar("versions", 255)
    val followers = integer("followers").clientDefault { 0 }
    val verifyId = varchar("verification_id", 100).nullable()
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val serverInfo = reference("server_info_id", ServersInfo, onDelete = ReferenceOption.CASCADE)
    val user = optReference("user_id", Users)
}

class Server(id: EntityID<Int>) : IntEntity(id) {
    companion object: IntEntityClass<Server>(Servers)
    val hostname by Servers.hostname
    val port by Servers.port
    val modes by Servers.modes
    val versions by Servers.versions
    val followers by Servers.followers
    val verifyId by Servers.verifyId
    val createdAt by Servers.createdAt
    val serverInfo by ServerInfo referencedOn(Servers.serverInfo)
    val user by User optionalReferencedOn(Servers.user)
}