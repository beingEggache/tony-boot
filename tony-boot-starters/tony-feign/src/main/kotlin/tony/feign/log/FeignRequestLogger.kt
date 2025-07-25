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

package tony.feign.log

/**
 * Feign okhttp 请求日志拦截器
 *
 * @author tangli
 * @date 2023/05/25 19:48
 */
import jakarta.annotation.Priority
import java.net.URL
import java.time.LocalDateTime
import okhttp3.Connection
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.util.unit.DataSize
import tony.core.TRACE_ID_HEADER_NAME
import tony.core.utils.getLogger
import tony.core.utils.ifNullOrBlank
import tony.core.utils.mdcPutOrGetDefault
import tony.core.utils.removeLineBreak
import tony.core.utils.toInstant
import tony.feign.byteArray
import tony.feign.isTextMediaTypes
import tony.feign.okhttp.interceptor.NetworkInterceptor
import tony.feign.parseMediaType

/**
 * Feign okhttp 请求日志拦截器
 *
 * @author tangli
 * @date 2023/05/25 19:48
 */
@Priority(Int.MAX_VALUE)
internal class FeignLogInterceptor(
    private val feignRequestLogger: FeignRequestLogger,
    /**
     * request日志请求体长度, 超过只显示ContentType
     */
    private val requestBodyMaxSize: Long,
    /**
     * request日志响应体长度, 超过只显示ContentType
     */
    private val responseBodyMaxSize: Long,
) : NetworkInterceptor {
    private val logger = getLogger()

    init {
        logger.info(
            "Request log is enabled. " +
                "Request body size limit is ${DataSize.ofBytes(requestBodyMaxSize)}, " +
                "Response body size limit is ${DataSize.ofBytes(responseBodyMaxSize)} "
        )
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val startTime = LocalDateTime.now()

        val request = chain.request()
        val headers =
            request
                .headers
                .newBuilder()
                .add(TRACE_ID_HEADER_NAME, mdcPutOrGetDefault(TRACE_ID_HEADER_NAME))
                .build()

        val newRequest =
            request
                .newBuilder()
                .headers(headers)
                .build()

        val response = chain.proceed(newRequest)
        val elapsedTime =
            System.currentTimeMillis() -
                startTime
                    .toInstant()
                    .toEpochMilli()

        feignRequestLogger.requestLog(
            chain.connection(),
            newRequest,
            response,
            elapsedTime,
            requestBodyMaxSize,
            responseBodyMaxSize
        )
        return response
    }
}

/**
 * 默认ok http 请求日志记录器
 * @author tangli
 * @date 2023/09/13 19:35
 */
internal open class DefaultFeignRequestLogger : FeignRequestLogger {
    protected val logger: Logger =
        getLogger("request-logger")

    /**
     * 记录请求日志
     * @param [connection] 链接
     * @param [request] 请求
     * @param [response] 响应
     * @param [elapsedTime] 执行时间
     * @author Tony
     * @date 2023/09/12 19:10
     */
    override fun requestLog(
        connection: Connection?,
        request: Request,
        response: Response,
        elapsedTime: Long,
        requestBodyMaxSize: Long,
        responseBodyMaxSize: Long,
    ) {
        val url =
            request
                .url
                .toUri()
                .toURL()
        val resultCode = response.code
        val protocol = url.protocol
        val httpMethod = request.method
        val origin = url.origin
        val path = url.path
        val query =
            url
                .query
                .ifNullOrBlank("[null]")
        val requestHeaders =
            request
                .headers
                .names()
                .sortedBy { it }
                .associateWith {
                    request.header(it)
                }.entries
                .joinToString(";;") { "${it.key}:${it.value}" }
        val responseHeaders =
            response
                .headers
                .names()
                .sortedBy { it }
                .associateWith {
                    response.header(it)
                }.entries
                .joinToString(";;") { "${it.key}:${it.value}" }
        val requestBody =
            request.body?.run {
                body(
                    byteArray(),
                    contentType().toString(),
                    requestBodyMaxSize
                )
            }

        val responseBody =
            response
                .peekBody(
                    (
                        response
                            .body
                            .contentLength()
                    ).coerceAtLeast(0)
                ).run {
                    body(
                        bytes(),
                        contentType().toString(),
                        responseBodyMaxSize
                    )
                }

        val remoteIp =
            connection
                ?.socket()
                ?.inetAddress
                ?.hostAddress

        val logMessage =
            buildString {
                append("$elapsedTime|")
                append("$resultCode|")
                append("${HttpStatus.valueOf(response.code).name}|")
                append("$protocol|")
                append("$httpMethod|")
                append("$origin|")
                append("$path|")
                append("$query|")
                append("$requestHeaders|")
                append("$responseHeaders|")
                append("$requestBody|")
                append("$responseBody|")
                append(remoteIp)
            }

        logger.trace(logMessage)
    }

    private fun body(
        contentByteArray: ByteArray,
        contentType: String,
        bodyMaxSize: Long,
    ) =
        if (!isTextMediaTypes(parseMediaType(contentType))) {
            "[$contentType]"
        } else {
            contentByteArray.let { bytes ->
                val size = bytes.size.toLong()
                when {
                    size in 1..bodyMaxSize -> String(bytes).removeLineBreak()
                    size >= bodyMaxSize -> "[too long content, length = ${DataSize.ofBytes(size)}]"
                    else -> "[null]"
                }
            }
        }

    @Suppress("MemberVisibilityCanBePrivate")
    protected val URL.origin: String
        get() =
            run {
                val protocol = protocol
                val host = host
                val port = port
                "$protocol://$host${if (port == 80 || port == 443 || port < 0) "" else ":$port"}"
            }
}

/**
 * Feign okhttp 请求日志记录接口.
 * @author tangli
 * @date 2023/09/13 19:35
 */
public fun interface FeignRequestLogger {
    public fun requestLog(
        connection: Connection?,
        request: Request,
        response: Response,
        elapsedTime: Long,
        requestBodyMaxSize: Long,
        responseBodyMaxSize: Long,
    )
}
