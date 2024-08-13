/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony.web.log

/**
 * 请求日志记录接口.
 *
 * @author tangli
 * @date 2023/05/25 19:29
 */
import com.tony.ApiProperty
import com.tony.utils.getFromRootAsString
import com.tony.utils.getLogger
import com.tony.utils.removeLineBreak
import com.tony.web.WebContext
import com.tony.web.filter.RepeatReadRequestWrapper
import com.tony.web.log.`#Const`.BAD_REQUEST
import com.tony.web.log.`#Const`.INTERNAL_SERVER_ERROR
import com.tony.web.log.`#Const`.NULL
import com.tony.web.log.`#Const`.OK
import com.tony.web.log.`#Const`.PRECONDITION_FAILED
import com.tony.web.log.`#Const`.UNAUTHORIZED
import com.tony.web.log.`#Const`.logger
import com.tony.web.utils.headers
import com.tony.web.utils.isTextMediaTypes
import com.tony.web.utils.parsedMedia
import com.tony.web.utils.remoteIp
import com.tony.web.utils.status1xxInformational
import com.tony.web.utils.status2xxSuccessful
import com.tony.web.utils.status3xxRedirection
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.util.ContentCachingResponseWrapper

/**
 * 请求日志记录接口.
 *
 * @author tangli
 * @date 2023/05/25 19:29
 */
public fun interface RequestTraceLogger {
    /**
     * 请求跟踪日志
     * @param [request] 请求
     * @param [response] 响应
     * @param [elapsedTime] 执行时间
     * @param [requestBodyMaxSize] 请求正文最大尺寸
     * @param [responseBodyMaxSize] 响应体最大尺寸
     * @author tangli
     * @date 2024/08/13 15:53
     * @since 1.0.0
     */
    public fun requestTraceLog(
        request: RepeatReadRequestWrapper,
        response: ContentCachingResponseWrapper,
        elapsedTime: Long,
        requestBodyMaxSize: Int,
        responseBodyMaxSize: Int,
    )
}

@Suppress("ClassName")
internal object `#Const` {
    @JvmSynthetic
    internal const val OK: String = "OK"

    @JvmSynthetic
    internal const val INTERNAL_SERVER_ERROR: String = "INTERNAL_SERVER_ERROR"

    @JvmSynthetic
    internal const val PRECONDITION_FAILED: String = "PRECONDITION_FAILED"

    @JvmSynthetic
    internal const val BAD_REQUEST: String = "BAD_REQUEST"

    @JvmSynthetic
    internal const val UNAUTHORIZED: String = "UNAUTHORIZED"

    @JvmSynthetic
    internal const val NULL: String = "[null]"

    @JvmSynthetic
    @JvmField
    internal val logger: Logger = getLogger("trace-logger")
}

/**
 * 请求日志记录默认实现
 *
 * @author tangli
 * @date 2023/05/25 19:30
 */
internal class DefaultRequestTraceLogger : RequestTraceLogger {
    override fun requestTraceLog(
        request: RepeatReadRequestWrapper,
        response: ContentCachingResponseWrapper,
        elapsedTime: Long,
        requestBodyMaxSize: Int,
        responseBodyMaxSize: Int,
    ) {
        val requestBody = requestBody(request, requestBodyMaxSize)
        val responseBody = responseBody(response, responseBodyMaxSize)
        val resultCode = resultCode(responseBody, response)
        val resultStatus = resultStatus(resultCode)
        val protocol = request.scheme
        val httpMethod = request.method
        val origin =
            request
                .requestURL
                ?.toString() ?: ""
        val path =
            request
                .requestURI
                .removePrefix(WebContext.contextPath)
        val query = request.queryString ?: NULL
        val requestHeaders =
            request
                .headers
                .entries
                .joinToString(";;") { "${it.key}:${it.value}" }
        val responseHeaders =
            response
                .headers
                .entries
                .joinToString(";;") { "${it.key}:${it.value}" }
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
            |$requestHeaders|
            |$responseHeaders|
            |$requestBody|
            |$responseBody|
            |$remoteIp
            """.trimMargin()
        logger.trace(logStr.removeLineBreak())
    }

    private fun requestBody(
        request: RepeatReadRequestWrapper,
        requestBodyMaxSize: Int,
    ) = if (!isTextMediaTypes(request.parsedMedia)) {
        "[${request.contentType}]"
    } else if (request
            .method
            .equals(
                HttpMethod
                    .POST
                    .name(),
                true
            )
    ) {
        val bytes = request.contentAsByteArray
        when {
            bytes.isEmpty() -> NULL
            bytes.size <= requestBodyMaxSize -> String(bytes)
            else -> "[too long content, length = ${bytes.size}]"
        }
    } else {
        NULL
    }

    private fun responseBody(
        response: ContentCachingResponseWrapper,
        responseBodyMaxSize: Int,
    ) = if (!isTextMediaTypes(response.parsedMedia)) {
        "[${response.contentType}]"
    } else {
        response.contentAsByteArray.let { bytes ->
            val size = bytes.size
            when {
                size in 1..responseBodyMaxSize -> String(bytes)
                size >= responseBodyMaxSize -> "[too long content, length = $size]"
                else -> NULL
            }
        }
    }

    private fun resultCode(
        responseBody: String,
        response: HttpServletResponse,
    ): Int {
        val codeFromResponseDirectly =
            responseBody
                .getFromRootAsString("code")
                ?.toInt()
        return when {
            codeFromResponseDirectly != null -> codeFromResponseDirectly

            response.status2xxSuccessful ||
                response.status3xxRedirection ||
                response.status1xxInformational -> ApiProperty.okCode

            else -> response.status * 100
        }
    }

    private fun resultStatus(resultCode: Int): String =
        when (resultCode) {
            ApiProperty.okCode, HttpStatus.OK.value() -> OK
            ApiProperty.badRequestCode, HttpStatus.BAD_REQUEST.value() -> BAD_REQUEST
            ApiProperty.preconditionFailedCode -> PRECONDITION_FAILED
            ApiProperty.unauthorizedCode -> UNAUTHORIZED
            in 400 * 100..499 * 100, in 400..499 -> BAD_REQUEST
            in 100 * 100..199 * 100 -> OK
            else -> INTERNAL_SERVER_ERROR
        }
}
