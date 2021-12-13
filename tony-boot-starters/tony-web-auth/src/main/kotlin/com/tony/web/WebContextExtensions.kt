@file:Suppress("unused")

package com.tony.web

import com.tony.Env.beanByLazy

object WebContextExtensions {

    private val apiSession: ApiSession by beanByLazy()

    val WebContext.userId: String
        get() = apiSession.userId

    val WebContext.apiSession: ApiSession
        get() = WebContextExtensions.apiSession
}
