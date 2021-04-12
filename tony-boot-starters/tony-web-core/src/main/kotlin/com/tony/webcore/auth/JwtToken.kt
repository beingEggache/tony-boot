package com.tony.webcore.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.tony.core.utils.toDate
import com.tony.webcore.config.WebJwtProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.Date
import javax.annotation.Resource

@Component
@ConditionalOnWebApplication
@ConditionalOnExpression("\${web.jwt.enabled:false}")
object JwtToken {

    @JvmStatic
    private val algorithm by lazy {
        Algorithm.HMAC256(webJwtProperties.secret)
    }

    @JvmStatic
    private val expiredMinutes by lazy {
        webJwtProperties.expiredMinutes
    }

    private lateinit var webJwtProperties: WebJwtProperties

    @Suppress("unused")
    @Resource
    private fun prop(webJwtProperties: WebJwtProperties) {
        JwtToken.webJwtProperties = webJwtProperties
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
    internal fun parse(jwt: String) = JWT.require(algorithm).build().verify(jwt)

    @JvmStatic
    private fun getExpireAt() =
        LocalDateTime
            .now()
            .plusMinutes(expiredMinutes)
            .toDate()
}
