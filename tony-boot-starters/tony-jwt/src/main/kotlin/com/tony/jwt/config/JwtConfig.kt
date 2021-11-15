package com.tony.jwt.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.tony.utils.toDate
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.Date
import javax.annotation.Resource

@Configuration
@ConditionalOnWebApplication
@ConditionalOnExpression("'\${jwt.secret:}'.trim() > ''")
@EnableConfigurationProperties(JwtProperties::class)
internal class JwtConfig

@ConstructorBinding
@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val secret: String = "",
    val expiredMinutes: Long = 525600L
)

@ConditionalOnClass(JWT::class)
@ConditionalOnWebApplication
@ConditionalOnExpression("'\${jwt.secret:}'.trim() > ''")
@Component
object JwtToken {

    @JvmStatic
    private val algorithm by lazy {
        Algorithm.HMAC256(jwtProperties.secret)
    }

    @JvmStatic
    private val expiredMinutes by lazy {
        jwtProperties.expiredMinutes
    }

    private lateinit var jwtProperties: JwtProperties

    @Suppress("unused")
    @Resource
    private fun prop(webJwtProperties: JwtProperties) {
        jwtProperties = webJwtProperties
    }

    @Suppress("unused")
    @JvmStatic
    fun gen(
        vararg params: Pair<String, String?>
    ): String =
        JWT.create()
            .withIssuedAt(Date())
            .withExpiresAt(getExpireAt())
            .apply {
                params.forEach { (key, value) ->
                    withClaim(key, value)
                }
            }.sign(algorithm)

    @JvmStatic
    fun parse(jwt: String): DecodedJWT = JWT.require(algorithm).build().verify(jwt)

    @JvmStatic
    private fun getExpireAt() =
        LocalDateTime
            .now()
            .plusMinutes(expiredMinutes)
            .toDate()
}
