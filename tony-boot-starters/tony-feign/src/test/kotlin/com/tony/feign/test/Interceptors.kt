package com.tony.feign.test

import com.tony.SpringContexts
import com.tony.feign.genSign
import com.tony.feign.okhttp.interceptor.AppInterceptor
import com.tony.feign.sortRequestBody
import com.tony.feign.test.exception.SignInvalidException
import com.tony.utils.getFromRootAsString
import com.tony.utils.getLogger
import com.tony.utils.isBetween
import com.tony.utils.toLocalDateTime
import com.tony.web.filter.RepeatReadRequestWrapper.Companion.toRepeatRead
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.springframework.web.servlet.HandlerInterceptor
import java.time.LocalDateTime

open class ProcessByHeaderInterceptor(
    private val headerName: String
) : AppInterceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val beanMap = SpringContexts.getBeansOfType(ByHeaderRequestProcessor::class.java)
        val processedRequest = originRequest
            .header(headerName)
            ?.split(",")
            ?.onEach { it.trim() }
            ?.mapNotNull {
                beanMap[it]
            }
            ?.fold(originRequest) { request, processor ->
                processor.process(request)
            }
            ?.run {
                this.newBuilder().removeHeader(headerName).build()
            }

        return chain.proceed(processedRequest ?: originRequest)
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
