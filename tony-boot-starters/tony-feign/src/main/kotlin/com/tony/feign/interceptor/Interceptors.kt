/**
 * tony-dependencies
 * Interceptors
 *
 * TODO
 *
 * @author tangli
 * @since 2021/12/16 10:15
 */
package com.tony.feign.interceptor

import com.tony.Beans
import com.tony.utils.getLogger
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.getBean
import kotlin.reflect.jvm.jvmName

interface NetworkInterceptor : Interceptor

interface AppInterceptor : Interceptor

open class ProcessByHeaderInterceptor(
    private val headerName: String
) : AppInterceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val processorImpl = request.header(headerName)
        if (processorImpl.isNullOrBlank()) return chain.proceed(request)
        val processor =
            try {
                Beans.getBean<ByHeaderRequestProcessor>(processorImpl)
            } catch (e: NoSuchBeanDefinitionException) {
                getLogger(this::class.jvmName).warn(e.localizedMessage)
                null
            } ?: return chain.proceed(request)

        return processByProcessor(processorImpl, processor, request, chain)
    }

    private fun processByProcessor(
        processorImpl: String?,
        processor: ByHeaderRequestProcessor,
        request: Request,
        chain: Interceptor.Chain
    ): Response {
        getLogger().info("$processorImpl ${processor::class.jvmName} process start")
        val requestRemoveHeader = request.newBuilder().removeHeader(headerName).build()
        val newRequest = processor.process(requestRemoveHeader)
        val response = chain.proceed(newRequest)
        getLogger().info("$processorImpl ${processor::class.jvmName} process end")
        return response
    }
}

fun interface ByHeaderRequestProcessor {
    fun process(request: Request): Request
}
