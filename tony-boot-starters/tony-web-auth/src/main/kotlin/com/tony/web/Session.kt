package com.tony.web
/**
 *
 * @author tangli
 * @since 2021-04-20 11:12
 */
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.tony.jwt.JwtToken
import com.tony.utils.defaultIfBlank
import com.tony.utils.getLogger
import com.tony.web.WebContext.getOrPut
import com.tony.web.exception.UnauthorizedException
import org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST

/**
 * noop session. 获取用户标识永远抛出异常.
 *
 * @author tangli
 * @since 2023/5/25 15:15
 */
internal class NoopApiSession : ApiSession {
    override val userId: String
        get() = TODO("Not yet implemented")

    override val unauthorizedException: UnauthorizedException? = null
}

/**
 * jwt 实现的session.
 *
 * @author tangli
 * @since 2023/5/25 15:17
 */
internal class JwtApiSession : ApiSession {

    private val logger = getLogger()

    private val token: DecodedJWT
        get() = WebContext.current.getOrPut("token", SCOPE_REQUEST) {
            logger.debug("init token")
            try {
                JwtToken.parse(WebContext.request.getHeader("x-token").defaultIfBlank())
            } catch (e: JWTVerificationException) {
                logger.warn(e.message, e)
                throw UnauthorizedException("请登录")
            }
        }

    override val userId: String
        get() = WebContext.current.getOrPut("userId", SCOPE_REQUEST) {
            logger.debug("init userId")
            token.getClaim("userId")?.asString() ?: throw UnauthorizedException("请登录")
        }

    override val unauthorizedException: UnauthorizedException?
        get() = try {
            token
            null
        } catch (e: UnauthorizedException) {
            e
        }
}
