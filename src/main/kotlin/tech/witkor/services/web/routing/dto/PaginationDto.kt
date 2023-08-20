package tech.witkor.services.web.routing.dto

import io.ktor.server.request.*
import kotlinx.serialization.Serializable

@Serializable
data class PaginationDto(
    val page: Int,
    val size: Int
) {
    fun skip() : Long {
        return ((this.page - 1) * this.size).toLong()
    }
    companion object {
        fun from(request: ApplicationRequest): PaginationDto {
            val page: Int = request.queryParameters["page"]?.toIntOrNull()?:1
            val size: Int = request.queryParameters["size"]?.toIntOrNull()?:16
            return PaginationDto(page, size)
        }
    }
}