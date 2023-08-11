package com.tony.feign.log

import com.tony.feign.isTextMediaTypes
import com.tony.feign.okhttp.interceptor.NetworkInterceptor
import com.tony.feign.parsedMedia
import com.tony.feign.string
import com.tony.traceIdHeaderName
import com.tony.utils.defaultIfBlank
import com.tony.utils.getLogger
import com.tony.utils.mdcPutOrGetDefault
import com.tony.utils.removeLineBreak
import com.tony.utils.toInstant
import jakarta.annotation.Priority
import java.net.URL
import java.time.LocalDateTime
import okhttp3.Connection
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.slf4j.Logger
import org.springframework.http.HttpStatus

/**
 * feign okhttp 请求日志拦截器
 *
 * @author tangli
 * @since 2023/5/25 15:48
 */
@Priority(Int.MAX_VALUE)
internal class FeignLogInterceptor(
    private val feignRequestTraceLogger: FeignRequestTraceLogger,
) : NetworkInterceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val startTime = LocalDateTime.now()
        val request = chain.request()
        val response = chain.proceed(request)
        val elapsedTime = System.currentTimeMillis() - startTime.toInstant().toEpochMilli()

        val headers = request
            .headers
            .newBuilder()
            .add(traceIdHeaderName, mdcPutOrGetDefault(traceIdHeaderName))
            .build()

        val newRequest = request
            .newBuilder()
            .headers(headers)
            .build()

        feignRequestTraceLogger.log(chain.connection(), newRequest, response, elapsedTime)
        return response
    }
}

/**
 * 默认ok http 请求日志记录器
 *
 * @author tangli
 * @since 2023/5/25 15:48
 */
public open class DefaultFeignRequestTraceLogger : FeignRequestTraceLogger {

    @Suppress("MemberVisibilityCanBePrivate")
    protected val logger: Logger = getLogger("request-logger")

    override fun log(
        connection: Connection?,
        request: Request,
        response: Response,
        elapsedTime: Long,
    ) {
        val url = request.url.toUri().toURL()
        val resultCode = response.code
        val protocol = url.protocol
        val httpMethod = request.method
        val origin = url.origin
        val path = url.path
        val query = url.query.defaultIfBlank("[null]")
        val headers = request.headers
            .toMultimap()
            .toMap()
            .mapValues { it.value.joinToString() }
            .entries
            .joinToString(";;") { "${it.key}:${it.value}" }
        val requestBody = request.body?.run {
            if (isTextMediaTypes(parsedMedia)) {
                string()
            } else {
                "[${contentType()}]"
            }
        }
        val responseBody = response.peekBody((response.body?.contentLength() ?: 0).coerceAtLeast(0)).run {
            if (isTextMediaTypes(parsedMedia)) {
                string()
            } else {
                "[${contentType()}]"
            }
        }

        val remoteIp = connection?.socket()?.inetAddress?.hostAddress
        val logStr =
            """
            |$elapsedTime|
            |$resultCode|
            |${HttpStatus.valueOf(response.code).name}|
            |$protocol|
            |$httpMethod|
            |$origin|
            |$path|
            |$query|
            |$headers|
            |$requestBody|
            |$responseBody|
            |$remoteIp"""
                .trimMargin()
        logger.trace(logStr.removeLineBreak())
    }

    @Suppress("MemberVisibilityCanBePrivate")
    protected val URL.origin: String
        get() = run {
            val protocol = protocol
            val host = host
            val port = port
            "$protocol://$host${if (port == 80 || port == 443 || port < 0) "" else ":$port"}"
        }
}

/**
 * feign okhttp 请求日志记录接口.
 *
 * @author tangli
 * @since 2023/5/25 15:49
 */
public fun interface FeignRequestTraceLogger {
    public fun log(
        connection: Connection?,
        request: Request,
        response: Response,
        elapsedTime: Long,
    )
}
