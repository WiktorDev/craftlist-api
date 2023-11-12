package tech.witkor.services.web.services

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import tech.witkor.services.web.models.Token
import tech.witkor.services.web.models.Tokens
import tech.witkor.services.web.models.User
import tech.witkor.services.web.utilities.TokenType

class TokenService {
    fun findToken(token: String, tokenType: TokenType? = null, user: User? = null): Token? = transaction {
        Tokens.select{ Tokens.token eq token }
            .map {
                val item = Token.wrapRow(it)
                if (tokenType != null && item.tokenType != tokenType) null
                if (user != null && item.user != user) null
                item
            }
            .singleOrNull()
    }
    fun findByUser(user: User, tokenType: TokenType): Token? = transaction {
        Tokens.select {
            (Tokens.user eq user.id) and (Tokens.tokenType eq tokenType)
        }.map { Token.wrapRow(it) }.singleOrNull()
    }
    fun createToken(token: String, tokenType: TokenType, user: User?) {
        transaction {
            Token.new {
                this.token = token
                this.tokenType = tokenType
                this.user = user
            }
        }
    }
}