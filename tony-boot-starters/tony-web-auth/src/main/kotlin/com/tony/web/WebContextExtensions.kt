@file:Suppress("unused")

package com.tony.web

import com.tony.Beans.getBeanByLazy

object WebContextExtensions {

    private val apiSession: ApiSession by getBeanByLazy()

    val WebContext.userId: String
        get() = apiSession.userId

    @Suppress("UnusedReceiverParameter")
    val WebContext.apiSession: ApiSession
        get() = WebContextExtensions.apiSession
}
