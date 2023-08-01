/**
 * Interceptors
 *
 * @author tangli
 * @since 2021/12/16 10:15
 */
package com.tony.feign.interceptor

import com.tony.fromInternalHeaderName
import com.tony.traceIdHeaderName
import com.tony.utils.mdcPutOrGetDefault
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response

/**
 * okhttp网络层拦截器.
 *
 * 会暴露出 [Interceptor.Chain.connection]
 *
 * @author tangli
 * @since 2023/5/25 15:45
 */
public interface NetworkInterceptor : Interceptor

/**
 * okhttp应用层拦截器.
 *
 * @author tangli
 * @since 2023/5/25 15:47
 */
public interface AppInterceptor : Interceptor

/**
 * okhttp 全局请求头拦截器
 *
 * @author tangli
 * @since 2023/7/4 17:54
 */
public interface GlobalHeaderInterceptor : AppInterceptor {
    public fun headers(): MutableMap<String, String>

    private fun internalHeaders(): MutableMap<String, String> = mutableMapOf(
        fromInternalHeaderName to "true",
        traceIdHeaderName to mdcPutOrGetDefault(traceIdHeaderName)
    ).apply {
        putAll(headers())
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val internalHeaders = internalHeaders()
            .entries
            .fold(Headers.Builder()) { headerBuilder, entry ->
                headerBuilder.add(entry.key, entry.value)
                headerBuilder
            }
            .build()
        val request = chain.request()

        val headers = request
            .headers
            .newBuilder()
            .addAll(internalHeaders)
            .build()

        val newRequest = request
            .newBuilder()
            .headers(headers)
            .build()
        return chain.proceed(newRequest)
    }
}

internal class DefaultGlobalHeaderInterceptor : GlobalHeaderInterceptor {
    override fun headers(): MutableMap<String, String> = mutableMapOf()
}
