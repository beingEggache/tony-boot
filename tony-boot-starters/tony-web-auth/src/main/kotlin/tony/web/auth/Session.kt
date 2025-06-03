/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package tony.web.auth

/**
 * 默认ApiSession实现.
 * @author tangli
 * @date 2021-04-20 11:12
 */
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import tony.TOKEN_HEADER_NAME
import tony.jwt.JwtToken
import tony.utils.getLogger
import tony.utils.ifNullOrBlank
import tony.web.WebContext
import tony.web.WebContext.getOrPut
import tony.web.WebSession
import tony.web.exception.UnauthorizedException

/**
 * noop session. 获取用户标识永远抛出异常.
 *
 * @author tangli
 * @date 2023/05/25 19:15
 */
internal class NoopWebSession : WebSession {
    override val userId: String
        get() = TODO("Not yet implemented")
    override val tenantId: String
        get() = TODO("Not yet implemented")

    override val unauthorizedException: UnauthorizedException? = null
}

/**
 * jwt 实现的session.
 *
 * @author tangli
 * @date 2023/05/25 19:17
 */
internal class JwtWebSession : WebSession {
    private val logger = getLogger()

    private val token: DecodedJWT
        get() =
            WebContext.current.getOrPut("token") {
                logger.debug("init token")
                val jwtTokenString =
                    WebContext
                        .request
                        .getHeader(TOKEN_HEADER_NAME)
                        .ifNullOrBlank()
                try {
                    JwtToken.parse(jwtTokenString)
                } catch (e: JWTVerificationException) {
                    logger.warn("Jwt Token($jwtTokenString) verify failed.")
                    throw UnauthorizedException("请登录")
                }
            }

    override val userId: String
        get() =
            WebContext.current.getOrPut("userId") {
                logger.debug("init userId")
                token.getClaim("userId")?.asString() ?: throw UnauthorizedException("请登录")
            }
    override val tenantId: String
        get() = TODO("Not yet implemented")

    override val unauthorizedException: UnauthorizedException?
        get() =
            try {
                token
                null
            } catch (e: UnauthorizedException) {
                e
            }
}
