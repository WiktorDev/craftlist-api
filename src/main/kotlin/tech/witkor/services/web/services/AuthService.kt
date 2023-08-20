package tech.witkor.services.web.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import tech.witkor.services.web.repositories.ExposedUser
import java.util.*

class AuthService() {
    fun createToken(user: ExposedUser): String {
        return JWT.create()
            .withAudience("")
            .withIssuer("")
            .withClaim("email", user.email)
            .withExpiresAt(Date(System.currentTimeMillis()))
            .sign(Algorithm.HMAC256(""))
    }
}