@file:Suppress("unused")

package com.tony.web

import com.tony.Beans.getBeanByLazy

public object WebContextExtensions {

    private val apiSession: ApiSession by getBeanByLazy()

    public val WebContext.userId: String
        get() = apiSession.userId

    @Suppress("UnusedReceiverParameter")
    public val WebContext.apiSession: ApiSession
        get() = WebContextExtensions.apiSession
}
