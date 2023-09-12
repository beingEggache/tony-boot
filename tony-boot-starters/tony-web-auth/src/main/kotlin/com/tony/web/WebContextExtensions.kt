package com.tony.web

import com.tony.SpringContexts.getBeanByLazy

/**
 * WebContext 扩展.
 *
 * 增加了获取用户标识和session对象的方法.
 *
 * @author Tang Li
 * @date 2023/5/25 15:18
 */
public object WebContextExtensions {

    @JvmStatic
    private val apiSession: ApiSession by getBeanByLazy()

    /**
     * 用户标识
     */
    @get:JvmSynthetic
    @JvmStatic
    public val WebContext.userId: String
        get() = apiSession.userId

    /**
     * Api session
     */
    @Suppress("UnusedReceiverParameter")
    @get:JvmSynthetic
    @JvmStatic
    public val WebContext.apiSession: ApiSession
        get() = WebContextExtensions.apiSession

    /**
     * apiSession
     * @return apiSession
     */
    @JvmStatic
    public fun apiSession(): ApiSession = apiSession

    /**
     * 用户标识
     * @return 用户标识
     */
    @JvmStatic
    public fun userId(): String = apiSession.userId
}
