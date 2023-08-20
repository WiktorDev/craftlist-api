package tech.witkor.services.web.routing.dto

import io.konform.validation.Validation
import io.konform.validation.jsonschema.minItems
import io.konform.validation.jsonschema.minLength
import io.ktor.server.plugins.*
import kotlinx.serialization.Serializable
import tech.witkor.services.web.utilities.ServerMode

@Serializable
data class CreateServerDto(
    val address: String,
    val versions: List<String>,
    val modes: List<ServerMode>
) {
    fun validate(): CreateServerDto {
        Validation<CreateServerDto> {
            CreateServerDto::address {
                minLength(3) hint "Podany adres jest zbyt krótki!"
            }
            CreateServerDto::versions {
                minItems(1) hint "Wybierz przynajmniej jedna wersje!"
            }
            CreateServerDto::modes {
                minItems(1) hint "Wybierz przynajmniej jeden tryb!"
            }
        }.validate(this).errors.forEach {
            throw BadRequestException(it.message)
        }
        return this
    }
}