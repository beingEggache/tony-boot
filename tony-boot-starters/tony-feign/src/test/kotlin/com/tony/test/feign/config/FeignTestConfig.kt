package com.tony.test.feign.config

import com.tony.TOKEN_HEADER_NAME
import com.tony.feign.genSign
import com.tony.feign.interceptor.request.RequestProcessor
import com.tony.feign.sortRequestBody
import com.tony.utils.getLogger
import com.tony.utils.ifNullOrBlank
import com.tony.utils.jsonNode
import com.tony.utils.toString
import feign.RequestTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime

/**
 * FeignTestConfig is
 * @author tangli
 * @date 2023/11/27 10:43
 * @since 1.0.0
 */
@Configuration
class FeignTestConfig {
    @Bean
    fun addTokenRequestProcessor(): AddTokenRequestProcessor =
        AddTokenRequestProcessor()

    @Bean
    fun signatureRequestProcessor(): SignatureRequestProcessor {
        return SignatureRequestProcessor()
    }
}


val mockWebAttributes = mutableMapOf<String, String>()

class AddTokenRequestProcessor : RequestProcessor {
    override fun invoke(template: RequestTemplate) {
        template.header(TOKEN_HEADER_NAME, mockWebAttributes["token"].ifNullOrBlank())
    }
}

class SignatureRequestProcessor : RequestProcessor {

    private val logger = getLogger()
    override operator fun invoke(template: RequestTemplate) {
        val body = template.body()
        if (body?.isNotEmpty() != true) {
            return
        }
        val appId = "appId"
        val secret = "secret"

        val timestampStr = LocalDateTime.now().toString("yyyy-MM-dd HH:mm:ss")
        logger.info("timestampStr:$timestampStr")
        val sortedJsonStr = body.jsonNode().sortRequestBody(timestampStr)
        logger.info("sortedJsonStr:$sortedJsonStr")
        val sign = sortedJsonStr.genSign(appId, secret)

        template
            .header("X-App-Id", appId)
            .header("X-Signature", sign)
            .header("X-Timestamp", timestampStr)
            .body(sortedJsonStr)
    }
}
