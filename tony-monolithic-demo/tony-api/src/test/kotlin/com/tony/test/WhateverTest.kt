package com.tony.test

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.tony.utils.println
import java.util.Date

/**
 *
 * @author tangli
 * @since 2020-11-05 11:31
 */
fun main() {
    gen("userId" to "99efd6bbc03b491191ca3206bd20046f").println()
}

fun gen(
    vararg params: Pair<String, String?>
): String =
    JWT.create()
        .withIssuedAt(Date())
        .apply {
            params.forEach { (key, value) ->
                withClaim(key, value)
            }
        }.sign(Algorithm.HMAC256("99efd6bbc03b491191ca3206bd20046f"))
