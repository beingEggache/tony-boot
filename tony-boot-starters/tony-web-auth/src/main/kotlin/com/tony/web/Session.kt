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

package com.tony.web
/**
 * 默认ApiSession实现.
 * @author Tang Li
 * @date 2021-04-20 11:12
 */
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.tony.jwt.JwtToken
import com.tony.utils.getLogger
import com.tony.utils.ifNullOrBlank
import com.tony.web.WebContext.getOrPut
import com.tony.web.exception.UnauthorizedException
import org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST

/**
 * noop session. 获取用户标识永远抛出异常.
 *
 * @author Tang Li
 * @date 2023/5/25 15:15
 */
internal class NoopApiSession : ApiSession {
    override val userId: String
        get() = TODO("Not yet implemented")

    override val unauthorizedException: UnauthorizedException? = null
}

/**
 * jwt 实现的session.
 *
 * @author Tang Li
 * @date 2023/5/25 15:17
 */
internal class JwtApiSession : ApiSession {
    private val logger = getLogger()

    private val token: DecodedJWT
        get() =
            WebContext.current.getOrPut("token", SCOPE_REQUEST) {
                logger.debug("init token")
                try {
                    JwtToken.parse(
                        WebContext
                            .request
                            .getHeader("x-token")
                            .ifNullOrBlank()
                    )
                } catch (e: JWTVerificationException) {
                    logger.warn(e.message, e)
                    throw UnauthorizedException("请登录")
                }
            }

    override val userId: String
        get() =
            WebContext.current.getOrPut("userId", SCOPE_REQUEST) {
                logger.debug("init userId")
                token.getClaim("userId")?.asString() ?: throw UnauthorizedException("请登录")
            }

    override val unauthorizedException: UnauthorizedException?
        get() =
            try {
                token
                null
            } catch (e: UnauthorizedException) {
                e
            }
}
