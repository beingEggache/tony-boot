/**
 *
 * @author tangli
 * @since 2021-04-20 11:12
 */
package com.tony.web

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.tony.jwt.JwtToken
import com.tony.utils.defaultIfBlank
import com.tony.utils.getLogger
import com.tony.web.WebContext.getOrPut
import com.tony.web.exception.UnauthorizedException
import org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST

internal class NoopApiSession : ApiSession {
    override val userId: String
        get() = TODO("Not yet implemented")

    override val unauthorizedException: UnauthorizedException? = null
}

internal class JwtApiSession : ApiSession {

    private val logger = getLogger()

    private val token: DecodedJWT
        get() = WebContext.current.getOrPut("token", SCOPE_REQUEST) {
            try {
                JwtToken.parse(WebContext.request.getHeader("x-token").defaultIfBlank())
            } catch (e: JWTVerificationException) {
                logger.warn(e.message)
                throw UnauthorizedException("请登录")
            }
        }

    override val userId: String
        get() = token.getClaim("userId")?.asString()
            ?: throw UnauthorizedException("请登录")

    override val unauthorizedException: UnauthorizedException?
        get() = try {
            token
            null
        } catch (e: UnauthorizedException) {
            e
        }
}
