package com.tony.test.feign.module.targeter_signature

import com.tony.annotation.EnableTonyBoot
import com.tony.feign.genSign
import com.tony.feign.interceptor.request.GlobalRequestInterceptorProvider
import com.tony.feign.interceptor.request.RequestProcessor
import com.tony.feign.interceptor.request.UseRequestProcessorsRequestInterceptor
import com.tony.feign.sortRequestBody
import com.tony.test.feign.SignatureInterceptor
import com.tony.utils.jsonNode
import com.tony.utils.toString
import feign.RequestInterceptor
import feign.RequestTemplate
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
class OpenFeignTestTargeterSignatureApp : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(SignatureInterceptor())
            .order(PriorityOrdered.HIGHEST_PRECEDENCE)
    }

    @Bean
    fun signatureRequestProcessor(): SignatureRequestProcessor {
        return SignatureRequestProcessor()
    }

    @Bean
    fun defaultGlobalRequestInterceptor(): GlobalRequestInterceptorProvider<RequestInterceptor> =
        GlobalRequestInterceptorProvider(UseRequestProcessorsRequestInterceptor())

}


class SignatureRequestProcessor : RequestProcessor {
    override operator fun invoke(template: RequestTemplate) {
        val body = template.body()
        if (body?.isNotEmpty() != true) {
            return
        }
        val appId = "appId"
        val secret = "secret"

        val timestampStr = LocalDateTime.now().toString("yyyy-MM-dd HH:mm:ss")
        val sortedJsonStr = body.jsonNode().sortRequestBody(timestampStr)
        val sign = sortedJsonStr.genSign(appId, secret)
        template
            .header("X-App-Id", appId)
            .header("X-Signature", sign)
            .header("X-Timestamp", timestampStr)
            .body(sortedJsonStr)
    }
}
