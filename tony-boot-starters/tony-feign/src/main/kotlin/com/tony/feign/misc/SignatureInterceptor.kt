/**
 * tony-dependencies
 * SignatureInterceptor
 *
 * TODO
 *
 * @author tangli
 * @since 2021/12/9 14:07
 */
package com.tony.feign.misc

import com.tony.Env
import com.tony.utils.getLogger
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import javax.annotation.Priority
import kotlin.reflect.jvm.jvmName

@FunctionalInterface
interface SignatureInterceptor {
    fun process(builder: Request.Builder): Request
}

@Priority(Int.MAX_VALUE - 1)
internal class SignatureFeignInterceptor : Interceptor {

    private val logger = getLogger()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val signatureImpl = request.header(SIGNATURE_IMPL)
        if (signatureImpl.isNullOrBlank()) return chain.proceed(request)
        val interceptor =
            try {
                Env.bean<SignatureInterceptor>(signatureImpl)
            } catch (e: NoSuchBeanDefinitionException) {
                logger.warn(e.localizedMessage)
                null
            } ?: return chain.proceed(request)

        logger.info("bean $signatureImpl<${interceptor::class.jvmName}> process start")
        val newRequest = interceptor.process(request.newBuilder().removeHeader(SIGNATURE_IMPL))
        val response = chain.proceed(newRequest)
        logger.info("bean $signatureImpl<${interceptor::class.jvmName}> process end")
        return response
    }
}

const val SIGNATURE_IMPL = "X-INTERNAL-SIGNATURE-IMPL"
