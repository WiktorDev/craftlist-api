package tech.witkor.services.web.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import tech.witkor.services.web.utilities.TokenType

object Tokens : IntIdTable("tokens") {
    val token = varchar("token", 128)
    val tokenType = enumerationByName("token_type", 32, TokenType::class)
    val user = optReference("user_id", Users)
}

class Token(id: EntityID<Int>) : IntEntity(id) {
    companion object: IntEntityClass<Token>(Tokens)
    var token by Tokens.token
    var tokenType by Tokens.tokenType
    var user by User optionalReferencedOn(Tokens.user)
}