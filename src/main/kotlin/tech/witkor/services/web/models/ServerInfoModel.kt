package tech.witkor.services.web.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ServersInfo : IntIdTable("servers_info") {
    val onlinePlayers = integer("online_players").clientDefault { 0 }
    val maxPlayers = integer("max_players").clientDefault { 0 }
}
class ServerInfo(id: EntityID<Int>) : IntEntity(id) {
    companion object: IntEntityClass<ServerInfo>(ServersInfo)

}