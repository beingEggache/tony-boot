/**
 *
 * @author tangli
 * @since 2021-04-20 11:12
 */
package com.tony.web

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.tony.ApiProperty
import com.tony.exception.ApiException
import com.tony.jwt.config.JwtToken
import com.tony.utils.defaultIfBlank
import com.tony.utils.getLogger
import com.tony.web.WebContext.getOrPut
import org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST

internal class NoopApiSession : ApiSession {
    override val userId: String
        get() = TODO("Not yet implemented")

    override fun genTokenString(vararg params: Pair<String, String?>): String {
        TODO("Not yet implemented")
    }

    override fun hasLogin(): Boolean = true
}

internal class JwtApiSession : ApiSession {

    private val logger = getLogger()

    private val token: DecodedJWT
        get() = WebContext.current.getOrPut("token", SCOPE_REQUEST) {
            try {
                JwtToken.parse(WebContext.request.getHeader("x-token").defaultIfBlank())
            } catch (e: JWTVerificationException) {
                logger.warn(e.message)
                throw ApiException("请登录", ApiProperty.unauthorizedCode)
            }
        }

    override val userId: String
        get() = token.getClaim("userId")?.asString()
            ?: throw ApiException("请登录", ApiProperty.unauthorizedCode)

    override fun genTokenString(vararg params: Pair<String, String?>) = JwtToken.gen(*params)

    @Suppress("SENSELESS_COMPARISON")
    override fun hasLogin() = token != null
}
