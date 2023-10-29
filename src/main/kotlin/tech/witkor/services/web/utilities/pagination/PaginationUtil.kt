package tech.witkor.services.web.utilities.pagination

import io.ktor.server.application.*

fun <T> createPagination(items: List<T>, call: ApplicationCall): PagedEntity<T> {
    return PagedEntity<T>(items, paginationParams(call))
}
fun <T> createPagination(items: List<T>, dto: PaginationDto): PagedEntity<T> {
    return PagedEntity<T>(items, dto)
}
fun paginationParams(call: ApplicationCall): PaginationDto {
    val page: Int = call.request.queryParameters["page"]?.toIntOrNull()?:1
    val size: Int = call.request.queryParameters["size"]?.toIntOrNull()?:16
    return PaginationDto(page, size)
}