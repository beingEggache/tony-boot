package com.tony.webcore.log

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonToken
import com.tony.core.ApiCode
import com.tony.core.utils.getLogger
import com.tony.core.utils.removeLineBreak
import com.tony.core.utils.toJsonString
import com.tony.webcore.WebContext
import com.tony.webcore.utils.headers
import com.tony.webcore.utils.isTextMediaTypes
import com.tony.webcore.utils.parsedMedia
import com.tony.webcore.utils.remoteIp
import org.springframework.http.HttpMethod
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

interface RequestTraceLogger {

    fun requestTraceLog(
        request: ContentCachingRequestWrapper,
        response: ContentCachingResponseWrapper,
        startTimeStr: String,
        elapsedTime: Long
    )
}

internal class DefaultRequestTraceLogger : RequestTraceLogger {

    private val logger = getLogger("request-trace-log")

    override fun requestTraceLog(
        request: ContentCachingRequestWrapper,
        response: ContentCachingResponseWrapper,
        startTimeStr: String,
        elapsedTime: Long
    ) {
        val url = request.requestURL?.toString() ?: ""
        val path = request.requestURI.removePrefix(WebContext.contextPath)
        val httpMethod = request.method
        val remoteIp = request.remoteIp
        val localIp = request.localAddr
        val query = request.queryString ?: NULL
        val requestParam = requestParam(request)
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
            |$url|
            |$path|
            |$query|
            |$headers|
            |$requestParam|
            |$responseBody|
            |$remoteIp|
            |$localIp""".trimMargin()
        logger.trace(logStr.removeLineBreak())
    }

    private fun requestParam(request: ContentCachingRequestWrapper) =
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
            HTTP_SUCCESS_CODE.contains(status) -> ApiCode.successCode
            else -> ApiCode.errorCode
        }
    }

    private fun resultStatus(resultCode: Int): String {
        return when (resultCode) {
            ApiCode.successCode -> SUCCESS
            ApiCode.validationErrorCode -> VALIDATE_FAILED
            ApiCode.bizErrorCode -> BIZ_FAILED
            ApiCode.unauthorizedCode -> UNAUTHORIZED
            in 400 * 100..499 * 100 -> VALIDATE_FAILED
            else -> FAILED
        }
    }
}

private val jsonFactory = JsonFactory()
private fun String.codeFromResponseDirectly(field: String): Int? {
    jsonFactory.createParser(this).use {
        while (try {
            it.nextToken()
        } catch (e: JsonParseException) {
                return ApiCode.errorCode
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
