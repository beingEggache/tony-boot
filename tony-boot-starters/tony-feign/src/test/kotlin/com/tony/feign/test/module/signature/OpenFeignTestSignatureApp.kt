package com.tony.feign.test.module.signature

import com.tony.annotation.EnableTonyBoot
import com.tony.feign.genSign
import com.tony.feign.jsonNode
import com.tony.feign.sortRequestBody
import com.tony.feign.test.ByHeaderRequestProcessor
import com.tony.feign.test.ProcessByHeaderInterceptor
import com.tony.feign.test.SignatureInterceptor
import com.tony.utils.getLogger
import com.tony.utils.toString
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.core.PriorityOrdered
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
