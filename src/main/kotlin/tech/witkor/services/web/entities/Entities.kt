package tech.witkor.services.web.entities

import kotlinx.serialization.Serializable

@Serializable
data class ErrorEntity(val code: Int, val message: String?);