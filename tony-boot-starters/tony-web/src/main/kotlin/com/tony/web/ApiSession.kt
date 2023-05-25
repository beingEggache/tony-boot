/**
 *
 * @author tangli
 * @since 2021-04-20 11:12
 */
package com.tony.web

import com.tony.web.exception.UnauthorizedException

/**
 * 自定义实现 session.
 *
 * @author tangli
 * @since 2023/5/25 10:52
 */
public interface ApiSession {

    /**
     * 用户标识
     */
    public val userId: String

    /**
     * 未认证异常
     */
    public val unauthorizedException: UnauthorizedException?
        get() = null
}
