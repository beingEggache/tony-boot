@file:Suppress("unused")

package com.tony.web

import com.tony.Env

object WebContextExtensions {

    private val apiSession: ApiSession by lazy {
        Env.bean()
    }

    val WebContext.userId: String
        get() = apiSession.userId

    val WebContext.apiSession: ApiSession
        get() = WebContextExtensions.apiSession
}
