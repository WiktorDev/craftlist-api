package tech.witkor.services.web.routing.servers.entities

import kotlinx.serialization.Serializable

@Serializable
data class TakeOverEntity(
    val id: String,
    val taked: Boolean
)