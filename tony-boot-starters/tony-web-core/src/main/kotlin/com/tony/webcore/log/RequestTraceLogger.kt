package com.tony.webcore.log

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonToken
import com.tony.core.ApiProperty
import com.tony.core.utils.getLogger
import com.tony.core.utils.removeLineBreak
import com.tony.core.utils.toJsonString
import com.tony.webcore.WebContext
import com.tony.webcore.log.RequestTraceLogger.Const.BIZ_FAILED
import com.tony.webcore.log.RequestTraceLogger.Const.FAILED
import com.tony.webcore.log.RequestTraceLogger.Const.HTTP_SUCCESS_CODE
import com.tony.webcore.log.RequestTraceLogger.Const.NULL
import com.tony.webcore.log.RequestTraceLogger.Const.SUCCESS
import com.tony.webcore.log.RequestTraceLogger.Const.UNAUTHORIZED
import com.tony.webcore.log.RequestTraceLogger.Const.VALIDATE_FAILED
import com.tony.webcore.utils.headers
import com.tony.webcore.utils.isTextMediaTypes
import com.tony.webcore.utils.parsedMedia
import com.tony.webcore.utils.remoteIp
import org.springframework.http.HttpMethod
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import javax.servlet.http.HttpServletResponse

interface RequestTraceLogger {

    fun requestTraceLog(
        request: ContentCachingRequestWrapper,
        response: ContentCachingResponseWrapper,
        startTimeStr: String,
        elapsedTime: Long
    )

    companion object Const {

        const val SUCCESS = "SUCCESS"

        const val FAILED = "FAILED"

        const val BIZ_FAILED = "BIZ_FAILED"

        const val VALIDATE_FAILED = "VALIDATE_FAILED"

        const val UNAUTHORIZED = "UNAUTHORIZED"

        const val NULL = "[null]"

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
        request: ContentCachingRequestWrapper,
        response: ContentCachingResponseWrapper,
        startTimeStr: String,
        elapsedTime: Long
    ) {
        val origin = request.requestURL?.toString() ?: ""
        val path = request.requestURI.removePrefix(WebContext.contextPath)
        val httpMethod = request.method
        val remoteIp = request.remoteIp
        val localIp = request.localAddr
        val query = request.queryString ?: NULL
        val requestBody = requestBody(request)
        val headers = request.headers.toJsonString()
        val responseBody = responseBody(response)
        val protocol = request.protocol
        val resultCode = resultCode(responseBody, response.status)
        val resultStatus = resultStatus(resultCode)
        val logStr =
            """
            |$startTimeStr|
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
            |$remoteIp|
            |$localIp""".trimMargin()
        logger.trace(logStr.removeLineBreak())
    }

    private fun requestBody(request: ContentCachingRequestWrapper) =
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
        val codeFromResponseDirectly = responseBody.codeFromResponseDirectly("code")
        return when {
            codeFromResponseDirectly != null -> codeFromResponseDirectly
            HTTP_SUCCESS_CODE.contains(status) -> ApiProperty.successCode
            else -> status * 100
        }
    }

    private fun resultStatus(resultCode: Int): String {
        return when (resultCode) {
            ApiProperty.successCode -> SUCCESS
            ApiProperty.validationErrorCode -> VALIDATE_FAILED
            ApiProperty.bizErrorCode -> BIZ_FAILED
            ApiProperty.unauthorizedCode -> UNAUTHORIZED
            in 400 * 100..499 * 100 -> VALIDATE_FAILED
            else -> FAILED
        }
    }

    private companion object {

        private val jsonFactory = JsonFactory()

        private val logger = getLogger("trace-logger")

        private val anotherLogger = getLogger()

        private fun String.codeFromResponseDirectly(field: String): Int? {
            jsonFactory.createParser(this).use {
                while (
                    try {
                        it.nextToken()
                    } catch (e: JsonParseException) {
                        anotherLogger.warn(""""$this" is not a json string.""")
                        return null
                    } != null
                ) {
                    if (it.currentToken == JsonToken.FIELD_NAME &&
                        it.currentName == field &&
                        it.parsingContext.parent.inRoot()
                    ) {
                        it.nextToken()
                        return it.valueAsString.toInt()
                    }
                }
            }
            return null
        }
    }
}
