package com.tony.feign.test.signature

import com.tony.Beans
import com.tony.annotation.EnableTonyBoot
import com.tony.feign.genSign
import com.tony.feign.interceptor.AppInterceptor
import com.tony.feign.jsonNode
import com.tony.feign.sortRequestBody
import com.tony.feign.test.exception.SignInvalidException
import com.tony.utils.getFromRootAsString
import com.tony.utils.getLogger
import com.tony.utils.isBetween
import com.tony.utils.toLocalDateTime
import com.tony.utils.toString
import com.tony.web.filter.RepeatReadRequestWrapper.Companion.toRepeatRead
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.core.PriorityOrdered
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.reflect.jvm.jvmName

@EnableFeignClients
@EnableTonyBoot
@SpringBootApplication
class OpenFeignTestSignatureApp : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(SignatureInterceptor())
            .order(PriorityOrdered.HIGHEST_PRECEDENCE)
    }

    @Bean
    fun processByHeaderInterceptor() =
        ProcessByHeaderInterceptor("X-Header-Process")

    @Bean
    fun byHeaderRequestProcessor() = SignatureRequestProcessor()
}

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


class SignatureInterceptor : HandlerInterceptor {
    private val logger = getLogger()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val repeatReadRequestWrapper = request.toRepeatRead()
        val bodyStr = String(repeatReadRequestWrapper.contentAsByteArray)

        val timestampStrFromHeader = request.getHeader("x-timestamp")
        if (timestampStrFromHeader.isNullOrBlank()) {
            logger.warn("Check signature: timestamp from  is null or blank.")
            throw SignInvalidException("验签失败")
        }

        val timestampStrFromReq = bodyStr.getFromRootAsString("timestamp")
        if (timestampStrFromReq.isNullOrBlank()) {
            logger.warn("Check signature: timestamp from request body is null or blank.")
            throw SignInvalidException("验签失败")
        }

        if (timestampStrFromHeader != timestampStrFromReq) {
            logger.warn("Check signature: timestamp from request body != timestamp from header")
            throw SignInvalidException("验签失败")
        }

        val requestTime = timestampStrFromHeader.toLocalDateTime("yyyy-MM-dd HH:mm:ss")

        val now = LocalDateTime.now()
        if (!now.isBetween(requestTime.minusSeconds(3 * 60), requestTime.plusSeconds(3 * 60))) {
            throw SignInvalidException("签名已过期")
        }

        val appId = request.getHeader("x-app-id")
        if (appId.isNullOrBlank()) {
            logger.warn("Check signature: appId from header is null or blank.")
            throw SignInvalidException("验签失败")
        }

        // TODO
        val secret = "secret"

        val signatureRemote = request.getHeader("x-signature")
        val signatureLocal = bodyStr.sortRequestBody(timestampStrFromHeader).genSign(appId, secret)

        logger.debug("Check signature: request body is {}.", bodyStr)
        logger.debug("Check signature: appId is {}.", appId)
        logger.debug("Check signature: signatureRemote is {}.", signatureRemote)
        logger.debug("Check signature: signatureLocal is {}.", signatureLocal)

        if (signatureRemote != signatureLocal) {
            throw SignInvalidException("验签失败")
        }

        return true
    }
}

class SignatureRequestProcessor : ByHeaderRequestProcessor {

    private val logger = getLogger()

    override fun process(request: Request): Request {
        val requestBody = request.body
        if (requestBody == null) {
            logger.warn("request body is null")
            return request
        }

        val appId = "appId"
        val secret = "secret"

        val timestampStr = LocalDateTime.now().toString("yyyy-MM-dd HH:mm:ss")
        val sortedJsonStr = requestBody.jsonNode().sortRequestBody(timestampStr)
        val sign = sortedJsonStr.genSign(appId, secret)
        return request.newBuilder()
            .addHeader("x-app-id", appId)
            .addHeader("x-signature", sign)
            .addHeader("x-timestamp", timestampStr)
            .method(request.method, sortedJsonStr.toRequestBody(requestBody.contentType()))
            .build()
    }
}
