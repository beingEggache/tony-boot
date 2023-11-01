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

package com.tony.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.tony.SpringContexts
import com.tony.jwt.config.JwtProperties
import com.tony.utils.toDate
import java.time.LocalDateTime
import java.util.Date

/**
 * jwt 单例类.
 * @author Tang Li
 * @date 2023/5/25 15:56
 */
public object JwtToken {
    @JvmStatic
    private val algorithm by lazy(LazyThreadSafetyMode.PUBLICATION) {
        Algorithm.HMAC256(jwtProperties.secret)
    }

    @JvmStatic
    private val expiredMinutes by lazy(LazyThreadSafetyMode.PUBLICATION) {
        jwtProperties.expiredMinutes
    }

    private val jwtProperties: JwtProperties by SpringContexts.getBeanByLazy()

    /**
     * 生成 jwt.
     * @param [params] claims 键值对.
     * @return [String]
     * @author Tang Li
     * @date 2023/09/28 10:50
     * @since 1.0.0
     */
    @JvmStatic
    public fun gen(vararg params: Pair<String, String?>): String =
        JWT
            .create()
            .withIssuedAt(Date())
            .withExpiresAt(getExpireAt())
            .apply {
                params.forEach { (key, value) ->
                    withClaim(key, value)
                }
            }.sign(algorithm)

    /**
     * 解析jwt.
     * @param [jwt] jwt
     * @return [DecodedJWT]
     * @author Tang Li
     * @date 2023/09/28 10:50
     * @since 1.0.0
     */
    @JvmStatic
    public fun parse(jwt: String): DecodedJWT =
        JWT
            .require(algorithm)
            .build()
            .verify(jwt)

    @JvmStatic
    private fun getExpireAt() =
        LocalDateTime
            .now()
            .plusMinutes(expiredMinutes)
            .toDate()
}
