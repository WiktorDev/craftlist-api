package tech.witkor.services.web.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ServersInfo : IntIdTable("servers_info") {
    val onlinePlayers = integer("online").clientDefault { 0 }
    val maxPlayers = integer("max_players").clientDefault { 0 }
}
class ServerInfo(id: EntityID<Int>) : IntEntity(id) {
    companion object: IntEntityClass<ServerInfo>(ServersInfo)
    var onlinePlayers by ServersInfo.onlinePlayers
    var maxPlayers by ServersInfo.maxPlayers
}

@Serializable
data class ExposedServerInfo(
    val online_players: Int,
    val max_players: Int
)

fun exposeServerInfo(serverInfo: ServerInfo): ExposedServerInfo {
    return ExposedServerInfo(
        serverInfo.onlinePlayers,
        serverInfo.maxPlayers
    )
}