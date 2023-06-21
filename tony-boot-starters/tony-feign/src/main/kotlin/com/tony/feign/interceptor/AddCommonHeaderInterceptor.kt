package com.tony.feign.interceptor

import com.tony.fromInternalHeaderName
import okhttp3.Interceptor
import okhttp3.Response

/**
 * AddCommonHeaderInterceptor is
 * @author tangli
 * @since 2023/06/21 14:14
 */
public class AddCommonHeaderInterceptor : AppInterceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder().addHeader(fromInternalHeaderName, "true").build()
        return chain.proceed(newRequest)
    }
}
