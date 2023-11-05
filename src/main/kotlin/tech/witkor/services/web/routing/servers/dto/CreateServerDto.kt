package tech.witkor.services.web.routing.servers.dto

import io.konform.validation.Validation
import io.konform.validation.jsonschema.minItems
import io.konform.validation.jsonschema.pattern
import kotlinx.serialization.Serializable
import tech.witkor.services.web.utilities.ServerMode
import tech.witkor.services.web.utilities.VersionsEnum
import tech.witkor.services.web.utilities.validation.ValidationHelper

@Serializable
data class CreateServerDto(
    val hostname: String = "",
    val port: Int = 25565,
    val versions: List<VersionsEnum> = listOf(),
    val modes: List<ServerMode> = listOf()
) {
    fun validate(): CreateServerDto {
        ValidationHelper(Validation {
            CreateServerDto::hostname {
                pattern(Regex("^(?!-)[A-Za-z0-9-]{1,63}(?<!-)(\\.[A-Za-z0-9-]{1,63})+\$")) hint "Podaj poprawny adres serwera"
            }
            CreateServerDto::port ifPresent {

            }
            CreateServerDto::versions {
                minItems(1) hint "Wybierz przynajmniej jedna wersje!"
            }
            CreateServerDto::modes {
                minItems(1) hint "Wybierz przynajmniej jeden tryb!"
            }
        }.validate(this)).throwError()
        return this
    }
}