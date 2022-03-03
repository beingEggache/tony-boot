/**
 *
 * @author tangli
 * @since 2021-04-20 11:12
 */
package com.tony.web

interface ApiSession {

    val userId: String

    fun genTokenString(vararg params: Pair<String, String?>): String

    fun hasLogin(): Boolean
}
