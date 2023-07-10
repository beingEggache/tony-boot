package com.tony.web.log

import com.tony.ApiProperty
import com.tony.utils.getFromRootAsString
import com.tony.utils.getLogger
import com.tony.utils.removeLineBreak
import com.tony.utils.toJsonString
import com.tony.web.WebApp
import com.tony.web.filter.RepeatReadRequestWrapper
import com.tony.web.log.RequestTraceLogger.Const.BAD_REQUEST
import com.tony.web.log.RequestTraceLogger.Const.INTERNAL_SERVER_ERROR
import com.tony.web.log.RequestTraceLogger.Const.NULL
import com.tony.web.log.RequestTraceLogger.Const.OK
import com.tony.web.log.RequestTraceLogger.Const.PRECONDITION_FAILED
import com.tony.web.log.RequestTraceLogger.Const.UNAUTHORIZED
import com.tony.web.log.RequestTraceLogger.Const.logger
import com.tony.web.utils.headers
import com.tony.web.utils.isTextMediaTypes
import com.tony.web.utils.parsedMedia
import com.tony.web.utils.remoteIp
import com.tony.web.utils.status1xxInformational
import com.tony.web.utils.status2xxSuccessful
import com.tony.web.utils.status3xxRedirection
import javax.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.springframework.http.HttpMethod
import org.springframework.web.util.ContentCachingResponseWrapper

/**
 * 请求日志记录接口.
 *
 * @author tangli
 * @since 2023/5/25 10:29
 */
public fun interface RequestTraceLogger {

    /**
     * 记录请求日志
     *
     * @param request
     * @param response
     * @param elapsedTime 执行时间
     */
    public fun requestTraceLog(
        request: RepeatReadRequestWrapper,
        response: ContentCachingResponseWrapper,
        elapsedTime: Long,
    )

    public companion object Const {

        public const val OK: String = "OK"

        public const val INTERNAL_SERVER_ERROR: String = "INTERNAL_SERVER_ERROR"

        public const val PRECONDITION_FAILED: String = "PRECONDITION_FAILED"

        public const val BAD_REQUEST: String = "BAD_REQUEST"

        public const val UNAUTHORIZED: String = "UNAUTHORIZED"

        public const val NULL: String = "[null]"

        public val logger: Logger = getLogger("trace-logger")
    }
}

/**
 * 请求日志记录默认实现
 *
 * @author tangli
 * @since 2023/5/25 10:30
 */
internal class DefaultRequestTraceLogger : RequestTraceLogger {

    override fun requestTraceLog(
        request: RepeatReadRequestWrapper,
        response: ContentCachingResponseWrapper,
        elapsedTime: Long,
    ) {
        val requestBody = requestBody(request)
        val responseBody = responseBody(response)
        val resultCode = resultCode(responseBody, response)
        val resultStatus = resultStatus(resultCode)
        val protocol = request.scheme
        val httpMethod = request.method
        val origin = request.requestURL?.toString() ?: ""
        val path = request.requestURI.removePrefix(WebApp.contextPath)
        val query = request.queryString ?: NULL
        val headers = request.headers.toJsonString()
        val remoteIp = request.remoteIp
        val logStr =
            """
            |$elapsedTime|
            |$resultCode|
            |$resultStatus|
            |$protocol|
            |$httpMethod|
            |$origin|
            |$path|
            |$query|
            |$headers|
            |$requestBody|
            |$responseBody|
            |$remoteIp
            """.trimMargin()
        logger.trace(logStr.removeLineBreak())
    }

    private fun requestBody(request: RepeatReadRequestWrapper) =
        if (!isTextMediaTypes(request.parsedMedia)) {
            "[${request.contentType}]"
        } else if (request.method.equals(HttpMethod.POST.name, true)) {
            val bytes = request.contentAsByteArray
            when {
                bytes.isEmpty() -> NULL
                bytes.size <= 65535 -> String(bytes)
                else -> "[too long content, length = ${bytes.size}]"
            }
        } else {
            NULL
        }

    private fun responseBody(response: ContentCachingResponseWrapper) =
        if (!isTextMediaTypes(response.parsedMedia)) {
            "[${response.contentType}]"
        } else {
            response.contentAsByteArray.let { bytes ->
                val size = bytes.size
                when {
                    size in 1..65535 -> String(bytes)
                    size >= 65536 -> "[too long content, length = $size]"
                    else -> NULL
                }
            }
        }

    private fun resultCode(responseBody: String, response: HttpServletResponse): Int {
        val codeFromResponseDirectly = responseBody.getFromRootAsString("code")?.toInt()
        return when {
            codeFromResponseDirectly != null -> codeFromResponseDirectly
            response.status2xxSuccessful ||
                response.status3xxRedirection ||
                response.status1xxInformational -> ApiProperty.okCode

            else -> response.status * 100
        }
    }

    private fun resultStatus(resultCode: Int): String = when (resultCode) {
        ApiProperty.okCode -> OK
        ApiProperty.badRequestCode -> BAD_REQUEST
        ApiProperty.preconditionFailedCode -> PRECONDITION_FAILED
        ApiProperty.unauthorizedCode -> UNAUTHORIZED
        in 400 * 100..499 * 100 -> BAD_REQUEST
        in 100 * 100..199 * 100 -> OK
        else -> INTERNAL_SERVER_ERROR
    }
}
