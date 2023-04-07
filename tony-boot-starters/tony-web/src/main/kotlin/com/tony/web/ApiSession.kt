/**
 *
 * @author tangli
 * @since 2021-04-20 11:12
 */
package com.tony.web

import com.tony.web.exception.UnauthorizedException

public interface ApiSession {

    public val userId: String

    public fun genTokenString(vararg params: Pair<String, String?>): String

    public fun loginOk(): UnauthorizedException? = null
}
