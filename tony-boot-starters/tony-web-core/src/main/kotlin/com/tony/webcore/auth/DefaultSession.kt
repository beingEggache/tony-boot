/**
 *
 * @author tangli
 * @since 2021-04-20 11:12
 */
package com.tony.webcore.auth

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.tony.core.exception.ApiException
import com.tony.core.utils.defaultIfBlank
import com.tony.webcore.WebApp
import com.tony.webcore.WebContext

interface ApiSession {

    val token: DecodedJWT

    val userId: String
}

internal class DefaultApiSession : ApiSession {
    override val token: DecodedJWT
        get() = try {
            JwtToken.parse(WebContext.request.getHeader("x-token").defaultIfBlank())
        } catch (e: JWTVerificationException) {
            throw ApiException("请登录", WebApp.unauthorizedCode, e)
        }
    override val userId: String
        get() = token.getClaim("userId").asString()
            ?: throw ApiException("请登录", WebApp.unauthorizedCode)
}
