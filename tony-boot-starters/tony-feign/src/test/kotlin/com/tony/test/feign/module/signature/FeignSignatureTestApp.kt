package com.tony.test.feign.module.signature

import com.tony.annotation.EnableTonyBoot
import com.tony.feign.genSign
import com.tony.feign.interceptor.request.RequestProcessor
import com.tony.feign.sortRequestBody
import com.tony.test.feign.exception.SignInvalidException
import com.tony.utils.getFromRootAsString
import com.tony.utils.getLogger
import com.tony.utils.isBetween
import com.tony.utils.jsonNode
import com.tony.utils.toLocalDateTime
import com.tony.utils.toString
import com.tony.web.filter.RepeatReadRequestWrapper.Companion.toRepeatRead
import feign.RequestTemplate
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
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
class FeignSignatureTestApp : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(SignatureInterceptor())
            .order(PriorityOrdered.HIGHEST_PRECEDENCE)
    }

    @Bean
    fun signatureRequestProcessor(): SignatureRequestProcessor {
        return SignatureRequestProcessor()
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
