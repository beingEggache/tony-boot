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
import dev.blaauwendraad.masker.json.JsonMasker
import jakarta.annotation.Priority
import java.net.URL
import java.time.LocalDateTime
import okhttp3.Connection
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import tony.core.TRACE_ID_HEADER_NAME
import tony.core.log.DesensitizedLogger
import tony.core.utils.getLogger
import tony.core.utils.ifNullOrBlank
import tony.core.utils.mdcPutOrGetDefault
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
public open class DefaultFeignRequestLogger(
    override val enableDesensitized: Boolean,
    override val desensitizedFields: Set<String>,
    override val desensitizedRequestHeaders: Set<String>,
    override val desensitizedResponseHeaders: Set<String>,
) : FeignRequestLogger,
    DesensitizedLogger {
    protected val logger: Logger = getLogger("request-logger")

    override val jsonMasker: JsonMasker = jsonMasker()

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
            desensitizedHeaders(
                request
                    .headers
                    .names()
                    .sortedBy { it }
                    .associateWith {
                        request.header(it)
                    }.entries
            )
        val responseHeaders =
            desensitizedHeaders(
                response
                    .headers
                    .names()
                    .sortedBy { it }
                    .associateWith {
                        request.header(it)
                    }.entries
            )
        val requestContentType = request.body?.contentType().toString()
        val requestMediaType = parseMediaType(requestContentType)
        val requestBody =
            desensitizeBody(
                request.body?.byteArray(),
                requestContentType,
                requestBodyMaxSize,
                isTextMediaTypes(requestMediaType),
                MediaType.APPLICATION_JSON.includes(requestMediaType)
            )

        val responseContentType = response.body.contentType().toString()
        val responseMediaType = parseMediaType(responseContentType)
        val responseBody =
            desensitizeBody(
                response
                    .peekBody(
                        (response.body.contentLength()).coerceAtLeast(0)
                    ).bytes(),
                responseContentType,
                responseBodyMaxSize,
                isTextMediaTypes(responseMediaType),
                MediaType.APPLICATION_JSON.includes(responseMediaType)
            )
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
public interface FeignRequestLogger {
    /**
     * 请求日志
     * @param [connection] 连接
     * @param [request] 请求
     * @param [response] 响应
     * @param [elapsedTime] 经过时间
     * @author tangli
     * @date 2025/08/05 10:38
     */
    public fun requestLog(
        connection: Connection?,
        request: Request,
        response: Response,
        elapsedTime: Long,
        requestBodyMaxSize: Long,
        responseBodyMaxSize: Long,
    )
}
