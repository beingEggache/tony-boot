package com.tony.auth.extensions

import com.tony.auth.ApiSession
import com.tony.webcore.WebApp
import com.tony.webcore.WebContext
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component
object Extensions {

    private lateinit var apiSession: ApiSession

    @Resource
    private fun apiSession(apiSession: ApiSession) {
        Extensions.apiSession = apiSession
    }

    val WebContext.userId: String
        get() = apiSession.userId

    val WebApp.apiSession: ApiSession
        get() = Extensions.apiSession
}
