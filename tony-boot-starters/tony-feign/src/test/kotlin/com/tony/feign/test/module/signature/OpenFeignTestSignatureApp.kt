package com.tony.feign.test.module.signature

import com.tony.annotation.EnableTonyBoot
import com.tony.feign.genSign
import com.tony.feign.jsonNode
import com.tony.feign.sortRequestBody
import com.tony.feign.test.ByHeaderRequestProcessor
import com.tony.feign.test.ProcessByHeaderInterceptor
import com.tony.feign.test.exception.SignInvalidException
import com.tony.utils.getFromRootAsString
import com.tony.utils.getLogger
import com.tony.utils.isBetween
import com.tony.utils.toLocalDateTime
import com.tony.utils.toString
import com.tony.web.filter.RepeatReadRequestWrapper.Companion.toRepeatRead
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.core.PriorityOrdered
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.LocalDateTime

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
    fun signatureRequestProcessor() = SignatureRequestProcessor()

    @Bean
    fun printHeaderRequestProcessor() = PrintHeaderRequestProcessor()
}


class SignatureInterceptor : HandlerInterceptor {
    private val logger = getLogger()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val repeatReadRequestWrapper = request.toRepeatRead()
        val bodyStr = String(repeatReadRequestWrapper.contentAsByteArray)

        val timestampStrFromHeader = request.getHeader("X-Timestamp")
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

        val appId = request.getHeader("X-App-Id")
        if (appId.isNullOrBlank()) {
            logger.warn("Check signature: appId from header is null or blank.")
            throw SignInvalidException("验签失败")
        }

        // TODO
        val secret = "secret"

        val signatureRemote = request.getHeader("X-Signature")
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
            .addHeader("X-App-Id", appId)
            .addHeader("X-Signature", sign)
            .addHeader("X-Timestamp", timestampStr)
            .method(request.method, sortedJsonStr.toRequestBody(requestBody.contentType()))
            .build()
    }
}

class PrintHeaderRequestProcessor : ByHeaderRequestProcessor {
    override fun process(request: Request): Request {
        return request
            .apply { println(headers) }
            .newBuilder()
            .addHeader("X-Test", "true")
            .build()
    }

}
