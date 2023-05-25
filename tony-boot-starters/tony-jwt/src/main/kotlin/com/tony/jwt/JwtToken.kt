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
 * @author tangli
 * @since 2023/5/25 15:56
 */
public object JwtToken {

    @JvmStatic
    private val algorithm by lazy {
        Algorithm.HMAC256(jwtProperties.secret)
    }

    @JvmStatic
    private val expiredMinutes by lazy {
        jwtProperties.expiredMinutes
    }

    private val jwtProperties: JwtProperties by SpringContexts.getBeanByLazy()

    /**
     * 生成 jwt.
     * @param params claims 键值对.
     * @return
     */
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

    /**
     * 解析jwt.
     * @param jwt
     * @return
     */
    @JvmStatic
    public fun parse(jwt: String): DecodedJWT = JWT.require(algorithm).build().verify(jwt)

    @JvmStatic
    private fun getExpireAt() =
        LocalDateTime.now()
            .plusMinutes(expiredMinutes)
            .toDate()
}
