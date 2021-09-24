package com.tony.auth.extensions

import com.tony.auth.ApiSession
import com.tony.webcore.WebContext
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component
object WebContextExtensions {

    private lateinit var apiSession: ApiSession

    @Lazy
    @Resource
    private fun apiSession(apiSession: ApiSession) {
        WebContextExtensions.apiSession = apiSession
    }

    val WebContext.userId: String
        get() = apiSession.userId

    val WebContext.apiSession: ApiSession
        get() = WebContextExtensions.apiSession
}
