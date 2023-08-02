package com.tony.feign.test

import com.tony.SpringContexts
import com.tony.feign.okhttp.interceptor.AppInterceptor
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

open class ProcessByHeaderInterceptor(
    private val headerName: String
) : AppInterceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val beanMap = SpringContexts.getBeansOfType(ByHeaderRequestProcessor::class.java)
        val processedRequest = originRequest
            .header(headerName)
            ?.split(",")
            ?.onEach { it.trim() }
            ?.mapNotNull {
                beanMap[it]
            }
            ?.fold(originRequest) { request, processor ->
                processor.process(request)
            }
            ?.run {
                this.newBuilder().removeHeader(headerName).build()
            }

        return chain.proceed(processedRequest ?: originRequest)
    }
}

fun interface ByHeaderRequestProcessor {
    fun process(request: Request): Request
}
