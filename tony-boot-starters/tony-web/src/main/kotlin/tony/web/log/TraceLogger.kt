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

package tony.web.log

/**
 * 请求日志记录接口.
 *
 * @author tangli
 * @date 2023/05/25 19:29
 */
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.util.unit.DataSize
import org.springframework.web.util.ContentCachingResponseWrapper
import tony.ApiProperty
import tony.utils.getFromRootAsString
import tony.utils.getLogger
import tony.utils.removeLineBreak
import tony.web.WebContext
import tony.web.filter.RepeatReadRequestWrapper
import tony.web.log.`#Const`.NULL
import tony.web.log.`#Const`.logger
import tony.web.utils.headers
import tony.web.utils.isTextMediaTypes
import tony.web.utils.parsedMedia
import tony.web.utils.remoteIp
import tony.web.utils.status1xxInformational
import tony.web.utils.status2xxSuccessful
import tony.web.utils.status3xxRedirection

/**
 * trace日志记录接口.
 *
 * @author tangli
 * @date 2023/05/25 19:29
 */
public fun interface TraceLogger {
    /**
     * trace跟踪日志
     * @param [request] 请求
     * @param [response] 响应
     * @param [elapsedTime] 执行时间
     * @param [requestBodyMaxSize] 请求正文最大尺寸
     * @param [responseBodyMaxSize] 响应体最大尺寸
     * @author tangli
     * @date 2024/08/13 15:53
     */
    public fun traceLog(
        request: RepeatReadRequestWrapper,
        response: ContentCachingResponseWrapper,
        elapsedTime: Long,
        requestBodyMaxSize: Long,
        responseBodyMaxSize: Long,
    )
}

@Suppress("ClassName")
internal object `#Const` {
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
internal class DefaultTraceLogger : TraceLogger {
    override fun traceLog(
        request: RepeatReadRequestWrapper,
        response: ContentCachingResponseWrapper,
        elapsedTime: Long,
        requestBodyMaxSize: Long,
        responseBodyMaxSize: Long,
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
        requestBodyMaxSize: Long,
    ) =
        if (!isTextMediaTypes(request.parsedMedia)) {
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
            val size = bytes.size.toLong()
            when {
                bytes.isEmpty() -> NULL
                size <= requestBodyMaxSize -> String(bytes)
                else -> "[too long content, length = ${DataSize.ofBytes(size)}]"
            }
        } else {
            NULL
        }

    private fun responseBody(
        response: ContentCachingResponseWrapper,
        responseBodyMaxSize: Long,
    ) =
        if (!isTextMediaTypes(response.parsedMedia)) {
            "[${response.contentType}]"
        } else {
            response.contentAsByteArray.let { bytes ->
                val size = bytes.size.toLong()
                when {
                    size in 1..responseBodyMaxSize -> String(bytes)
                    size >= responseBodyMaxSize -> "[too long content, length = ${DataSize.ofBytes(size)}]"
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
            ApiProperty.okCode, HttpStatus.OK.value(), in 100 * 100..199 * 100 -> {
                HttpStatus.OK.name
            }

            ApiProperty.preconditionFailedCode -> {
                HttpStatus.PRECONDITION_FAILED.name
            }

            ApiProperty.badRequestCode, HttpStatus.BAD_REQUEST.value() -> {
                HttpStatus.BAD_REQUEST.name
            }

            ApiProperty.unauthorizedCode, HttpStatus.UNAUTHORIZED.value() -> {
                HttpStatus.UNAUTHORIZED.name
            }

            ApiProperty.notFoundCode, HttpStatus.NOT_FOUND.value(), in 404 * 100 until 405 * 100 -> {
                HttpStatus.NOT_FOUND
                    .name
            }

            in 400 * 100..499 * 100, in 400..499 -> {
                HttpStatus.BAD_REQUEST.name
            }

            else -> {
                HttpStatus.INTERNAL_SERVER_ERROR.name
            }
        }
}
