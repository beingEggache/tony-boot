package com.tony.jwt.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.tony.Beans.getBeanByLazy
import com.tony.utils.toDate
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import java.util.Date

@Configuration
@ConditionalOnWebApplication
@ConditionalOnExpression("'\${jwt.secret:}'.trim() > ''")
@EnableConfigurationProperties(JwtProperties::class)
internal class JwtConfig

@ConstructorBinding
@ConfigurationProperties(prefix = "jwt")
public data class JwtProperties(
    @DefaultValue("")
    val secret: String = "",
    /**
     * jwt token expired minutes, default value is one year.
     */
    @DefaultValue("525600")
    val expiredMinutes: Long = 525600L,
)

public object JwtToken {

    @JvmStatic
    private val algorithm by lazy {
        Algorithm.HMAC256(jwtProperties.secret)
    }

    @JvmStatic
    private val expiredMinutes by lazy {
        jwtProperties.expiredMinutes
    }

    private val jwtProperties: JwtProperties by getBeanByLazy()

    @JvmStatic
    public fun gen(
        vararg params: Pair<String, String?>,
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
    public fun parse(jwt: String): DecodedJWT = JWT.require(algorithm).build().verify(jwt)

    @JvmStatic
    private fun getExpireAt() =
        LocalDateTime
            .now()
            .plusMinutes(expiredMinutes)
            .toDate()
}
