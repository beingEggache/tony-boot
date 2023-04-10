package com.tony.web

import com.tony.SpringContexts.getBeanByLazy

public object WebContextExtensions {

    @JvmStatic
    private val apiSession: ApiSession by getBeanByLazy()

    @get:JvmSynthetic
    @JvmStatic
    public val WebContext.userId: String
        get() = apiSession.userId

    @Suppress("UnusedReceiverParameter")
    @get:JvmSynthetic
    @JvmStatic
    public val WebContext.apiSession: ApiSession
        get() = WebContextExtensions.apiSession

    @JvmStatic
    public fun apiSession(): ApiSession = apiSession

    @JvmStatic
    public fun userId(): String = apiSession.userId
}
