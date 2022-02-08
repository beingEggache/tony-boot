package com.tony.web.log

import com.tony.ApiProperty
import com.tony.utils.getFromRootAsString
import com.tony.utils.getLogger
import com.tony.utils.removeLineBreak
import com.tony.utils.toJsonString
import com.tony.web.WebContext
import com.tony.web.filter.RepeatReadRequestWrapper
import com.tony.web.log.RequestTraceLogger.Const.BIZ_FAILED
import com.tony.web.log.RequestTraceLogger.Const.FAILED
import com.tony.web.log.RequestTraceLogger.Const.HTTP_SUCCESS_CODE
import com.tony.web.log.RequestTraceLogger.Const.NULL
import com.tony.web.log.RequestTraceLogger.Const.SUCCESS
import com.tony.web.log.RequestTraceLogger.Const.UNAUTHORIZED
import com.tony.web.log.RequestTraceLogger.Const.VALIDATE_FAILED
import com.tony.web.log.RequestTraceLogger.Const.logger
import com.tony.web.utils.headers
import com.tony.web.utils.isTextMediaTypes
import com.tony.web.utils.parsedMedia
import com.tony.web.utils.remoteIp
import org.springframework.http.HttpMethod
import org.springframework.web.util.ContentCachingResponseWrapper
import javax.servlet.http.HttpServletResponse

fun interface RequestTraceLogger {

    fun requestTraceLog(
        request: RepeatReadRequestWrapper,
        response: ContentCachingResponseWrapper,
        elapsedTime: Long
    )

    companion object Const {

        const val SUCCESS = "SUCCESS"

        const val FAILED = "FAILED"

        const val BIZ_FAILED = "BIZ_FAILED"

        const val VALIDATE_FAILED = "VALIDATE_FAILED"

        const val UNAUTHORIZED = "UNAUTHORIZED"

        const val NULL = "[null]"

        val logger = getLogger("trace-logger")

        @JvmSynthetic
        internal val HTTP_SUCCESS_CODE = arrayOf(
            HttpServletResponse.SC_OK,
            HttpServletResponse.SC_CREATED,
            HttpServletResponse.SC_NOT_MODIFIED,
            HttpServletResponse.SC_MOVED_PERMANENTLY,
            HttpServletResponse.SC_MOVED_TEMPORARILY
        )
    }
}

internal class DefaultRequestTraceLogger : RequestTraceLogger {

    override fun requestTraceLog(
        request: RepeatReadRequestWrapper,
        response: ContentCachingResponseWrapper,
        elapsedTime: Long
    ) {
        val requestBody = requestBody(request)
        val responseBody = responseBody(response)
        val httpStatus = response.status
        val resultCode = resultCode(responseBody, httpStatus)
        val resultStatus = resultStatus(resultCode)
        val protocol = request.scheme
        val httpMethod = request.method
        val origin = request.requestURL?.toString() ?: ""
        val path = request.requestURI.removePrefix(WebContext.contextPath)
        val query = request.queryString ?: NULL
        val headers = request.headers.toJsonString()
        val remoteIp = request.remoteIp
        val logStr =
            """
            |$elapsedTime|
            |$resultCode|
            |$resultStatus|
            |$protocol|
            |$httpStatus|
            |$httpMethod|
            |$origin|
            |$path|
            |$query|
            |$headers|
            |$requestBody|
            |$responseBody|
            |$remoteIp""".trimMargin()
        logger.trace(logStr.removeLineBreak())
    }

    private fun requestBody(request: RepeatReadRequestWrapper) =
        if (!isTextMediaTypes(request.parsedMedia)) "[${request.getHeader("Content-Type")}]"
        else if (request.method.equals(HttpMethod.POST.name, true)) {
            val bytes = request.contentAsByteArray
            when {
                bytes.isEmpty() -> NULL
                bytes.size <= 65535 -> String(bytes)
                else -> "[too long content, length = ${bytes.size}]"
            }
        } else NULL

    private fun responseBody(response: ContentCachingResponseWrapper) =
        if (!isTextMediaTypes(response.parsedMedia)) "[${response.getHeader("Content-Type")}]"
        else response.contentAsByteArray.let { bytes ->
            val size = bytes.size
            when {
                size in 1..65535 -> String(bytes)
                size >= 65536 -> "[too long content, length = $size]"
                else -> NULL
            }
        }

    private fun resultCode(responseBody: String, status: Int): Int {
        val codeFromResponseDirectly = responseBody.getFromRootAsString("code")?.toInt()
        return when {
            codeFromResponseDirectly != null -> codeFromResponseDirectly
            HTTP_SUCCESS_CODE.contains(status) -> ApiProperty.successCode
            status in 100..199 -> ApiProperty.successCode
            else -> status * 100
        }
    }

    private fun resultStatus(resultCode: Int): String = when (resultCode) {
        ApiProperty.successCode -> SUCCESS
        ApiProperty.validationErrorCode -> VALIDATE_FAILED
        ApiProperty.bizErrorCode -> BIZ_FAILED
        ApiProperty.unauthorizedCode -> UNAUTHORIZED
        in 400 * 100..499 * 100 -> VALIDATE_FAILED
        else -> FAILED
    }
}
