package tech.witkor.services.web.utilities.pagination

import kotlinx.serialization.Serializable

@Serializable
data class PaginationDto(
    val page: Int,
    val size: Int
) {
    fun skip() : Long {
        return ((this.page - 1) * this.size).toLong()
    }
}
@Serializable
data class PagedEntity<T>(
    val items: List<T>,
    val pagination: PaginationDto
)