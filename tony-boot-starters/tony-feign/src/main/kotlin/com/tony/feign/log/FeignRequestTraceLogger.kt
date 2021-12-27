package com.tony.feign.log

import com.tony.feign.interceptor.NetworkInterceptor
import com.tony.feign.isTextMediaTypes
import com.tony.feign.parsedMedia
import com.tony.feign.string
import com.tony.utils.defaultIfBlank
import com.tony.utils.getLogger
import com.tony.utils.removeLineBreak
import com.tony.utils.toInstant
import com.tony.utils.toJsonString
import com.tony.utils.toString
import okhttp3.Connection
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.URL
import java.time.LocalDateTime
import javax.annotation.Priority

@Priority(Int.MAX_VALUE)
internal class FeignLogInterceptor(
    private val feignRequestTraceLogger: FeignRequestTraceLogger
) : NetworkInterceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val startTime = LocalDateTime.now()
        val startTimeStr = startTime.toString("yyyy-MM-dd HH:mm:ss.SSS")
        val request = chain.request()
        val response = chain.proceed(request)
        val elapsedTime = System.currentTimeMillis() - startTime.toInstant().toEpochMilli()

        feignRequestTraceLogger.log(chain.connection(), request, response, startTimeStr, elapsedTime)
        return response
    }
}

internal class DefaultFeignRequestTraceLogger : FeignRequestTraceLogger {

    private val logger = getLogger("request-logger")

    override fun log(
        connection: Connection?,
        request: Request,
        response: Response,
        startTime: String,
        elapsedTime: Long
    ) {
        val url = request.url.toUri().toURL()
        val resultCode = response.code
        val protocol = url.protocol
        val httpMethod = request.method
        val origin = url.origin
        val query = url.query.defaultIfBlank("[null]")
        val path = url.path
        val headers = request.headers.toMultimap().toMap().mapValues { it.value.joinToString() }.toJsonString()
        val responseBody = response.peekBody((response.body?.contentLength() ?: 0).coerceAtLeast(0)).run {
            if (isTextMediaTypes(parsedMedia)) string()
            else "[${contentType()}]"
        }
        val requestBody = request.body?.run {
            if (isTextMediaTypes(parsedMedia)) string()
            else "[${contentType()}]"
        }
        val remoteIp = connection?.socket()?.inetAddress?.hostAddress
        val localIp = connection?.socket()?.localAddress?.hostAddress
        val logStr =
            """
            |$startTime|
            |$elapsedTime|
            |$resultCode|
            |[null]|
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

    private val URL.origin: String
        get() = run {
            val protocol = protocol
            val host = host
            val port = port
            "$protocol://$host${if (port == 80 || port == 443 || port < 0) "" else ":$port"}"
        }
}

interface FeignRequestTraceLogger {
    fun log(
        connection: Connection?,
        request: Request,
        response: Response,
        startTime: String,
        elapsedTime: Long
    )
}
