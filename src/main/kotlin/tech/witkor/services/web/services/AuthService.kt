package tech.witkor.services.web.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.server.config.*
import tech.witkor.services.web.repositories.ExposedUser
import java.time.Instant
import java.util.concurrent.TimeUnit
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Serializable
data class AccessTokenRequest(
    val code: String
)
@Serializable
data class AccessTokenResponse(
    val accessToken: String
)
@Serializable
data class UserProfileResponse(
    val username: String,
    val email: String,
    val role: String,
    val canChangeName: Boolean
)

class AuthService(private val config: ApplicationConfig) : KoinComponent {
    private val httpClient : HttpClient by inject()

    fun property(property: String): String {
        return config.propertyOrNull("ktor.$property")?.getString() ?: ""
    }

    fun createToken(user: ExposedUser): String {
        return JWT.create()
            .withAudience("")
            .withIssuer("")
            .withClaim("email", user.email)
            .withExpiresAt(Instant.now().plus(10, TimeUnit.MINUTES.toChronoUnit()))
            .sign(Algorithm.HMAC256(this.property("jwt.secret")))
    }

    fun getLoginUrl(): String {
        return "https://id.yshop.pl/api/oauth2/authorize?clientId=${property("services.id.clientId")}&redirectUri=${property("services.id.redirectUri")}&scope=${property("services.id.scope")}"
    }

    suspend fun getToken(code: String): AccessTokenResponse? {
        val response = this.httpClient.post("https://id.yshop.pl/api/oauth2/token") {
            contentType(ContentType.parse("application/json"))
            headers {
                append("ClientId", property("services.id.clientId"))
                append("ClientSecret", property("services.id.clientSecret"))
            }
            setBody(AccessTokenRequest(code))
        }
        if (!response.status.isSuccess()) return null
        return response.body()
    }
    suspend fun getProfile(token: String): UserProfileResponse? {
        val response = this.httpClient.get("https://id.yshop.pl/api/auth/me?useHeaders=true") {
            headers {
                append("Authorization", "Bearer $token")
            }
        }
        if (!response.status.isSuccess()) return null
        return response.body()
    }
}