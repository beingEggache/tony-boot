/**
 *
 * @author tangli
 * @since 2021-04-20 11:12
 */
package com.tony.webcore.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.tony.core.exception.ApiException
import com.tony.core.utils.defaultIfBlank
import com.tony.core.utils.getLogger
import com.tony.core.utils.toDate
import com.tony.webcore.WebApp
import com.tony.webcore.WebContext
import com.tony.webcore.WebContext.getOrPut
import com.tony.webcore.config.WebJwtProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST
import java.time.LocalDateTime
import java.util.Date
import javax.annotation.Resource

interface ApiSession {

    val userId: String

    fun genTokenString(vararg params: Pair<String, String?>): String

    fun hasLogin(): Boolean
}

internal class JwtApiSession : ApiSession {

    private val logger = getLogger()

    private val token: DecodedJWT?
        get() = WebContext.current.getOrPut("token", SCOPE_REQUEST) {
            try {
                JwtToken.parse(WebContext.request.getHeader("x-token").defaultIfBlank())
            } catch (e: JWTVerificationException) {
                logger.warn(e.message)
                throw ApiException("请登录", WebApp.unauthorizedCode)
            }
        }

    override val userId: String
        get() = token?.getClaim("userId")?.asString()
            ?: throw ApiException("请登录", WebApp.unauthorizedCode)

    override fun genTokenString(vararg params: Pair<String, String?>) = JwtToken.gen(*params)

    override fun hasLogin() = token != null
}

@ConditionalOnClass(JWT::class)
@ConditionalOnWebApplication
@ConditionalOnExpression("\${web.jwt.enabled:false}")
@Component
internal object JwtToken {

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
