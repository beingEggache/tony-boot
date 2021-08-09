package com.tony.webcore.filter

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonToken
import com.tony.core.utils.defaultIfBlank
import com.tony.core.utils.defaultZoneOffset
import com.tony.core.utils.getLogger
import com.tony.core.utils.removeLineBreak
import com.tony.core.utils.toJsonString
import com.tony.core.utils.toString
import com.tony.webcore.WebApp
import com.tony.webcore.WebContext
import com.tony.webcore.config.WebProperties
import com.tony.webcore.utils.antPathMatcher
import com.tony.webcore.utils.headers
import com.tony.webcore.utils.isCorsPreflightRequest
import com.tony.webcore.utils.isTextMediaTypes
import com.tony.webcore.utils.matchAny
import com.tony.webcore.utils.parsedMedia
import com.tony.webcore.utils.remoteIp
import org.slf4j.MDC
import org.springframework.core.PriorityOrdered
import org.springframework.http.HttpMethod
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.IOException
import java.time.LocalDateTime
import java.util.UUID
import javax.annotation.Priority
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebFilter(filterName = "traceLoggingFilter", servletNames = ["dispatcherServlet"])
@Priority(PriorityOrdered.HIGHEST_PRECEDENCE + 101)
internal class TraceLoggingFilter(
    private val webProperties: WebProperties,
) : OncePerRequestFilter() {

    private val log = getLogger()

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) = doFilterTrace(
        ContentCachingRequestWrapper(request),
        ContentCachingResponseWrapper(response),
        filterChain,
        LocalDateTime.now()
    )

    @Throws(IOException::class, ServletException::class)
    private fun doFilterTrace(
        requestWrapper: ContentCachingRequestWrapper,
        responseWrapper: ContentCachingResponseWrapper,
        filterChain: FilterChain,
        startTime: LocalDateTime
    ) = try {
        filterChain.doFilter(requestWrapper, responseWrapper)
    } finally {
        val elapsedTime = System.currentTimeMillis() - startTime.toInstant(defaultZoneOffset).toEpochMilli()

        log(requestWrapper, responseWrapper, startTime, elapsedTime)

        responseWrapper.copyBodyToResponse()
    }

    private fun log(
        requestWrapper: ContentCachingRequestWrapper,
        responseWrapper: ContentCachingResponseWrapper,
        startTime: LocalDateTime,
        elapsedTime: Long
    ) = try {

        val url = requestWrapper.requestURL?.toString()
        val path = requestWrapper.requestURI.removePrefix(WebContext.contextPath)
        val httpMethod = requestWrapper.method
        val remoteIp = requestWrapper.remoteIp
        val localIp = requestWrapper.localAddr
        val query = requestWrapper.queryString ?: NULL
        val requestParam = requestParam(requestWrapper)
        val headers = requestWrapper.headers.toJsonString()
        val responseBody = responseBody(responseWrapper)
        val resultCode = resultCode(responseBody.defaultIfBlank(), responseWrapper.status)
        val resultStatus = when (resultCode) {
            webProperties.successCode -> SUCCESS
            webProperties.validationErrorCode -> VALIDATE_FAILED
            webProperties.bizErrorCode -> BIZ_FAILED
            webProperties.unauthorizedCode -> UNAUTHORIZED
            in 400 * 100..499 * 100 -> VALIDATE_FAILED
            else -> FAILED
        }
        val protocol = requestWrapper.protocol
        val startTimeStr = startTime.toString("yyyy-MM-dd HH:mm:ss.SSS")

        val logStr = startTimeStr +
            "|$elapsedTime" +
            "|$resultCode" +
            "|$resultStatus" +
            "|$protocol" +
            "|$httpMethod" +
            "|$url" +
            "|$path" +
            "|$query" +
            "|$headers" +
            "|$requestParam" +
            "|$responseBody" +
            "|$remoteIp" +
            "|$localIp"

        log.trace(logStr.removeLineBreak())
    } catch (e: Exception) {
        log.error(e.message, e)
    }

    private fun resultCode(responseBody: String, status: Int) = let {
        val codeFromResponseDirectly = responseBody.getJsonRootValue("code")
        when {
            codeFromResponseDirectly != null -> codeFromResponseDirectly.toInt()
            HTTP_SUCCESS_CODE.contains(status) -> webProperties.successCode
            else -> webProperties.errorCode
        }
    }

    override fun shouldNotFilter(request: HttpServletRequest) =
        antPathMatcher.matchAny(EXCLUDE_URLS, request.requestURI) || request.isCorsPreflightRequest

    private companion object {

        private const val BIZ_FAILED = "BIZ_FAILED"

        private val EXCLUDE_URLS by lazy {
            WebApp.excludeJsonResultUrlPatterns.plus(WebApp.ignoreUrlPatterns())
        }

        private const val FAILED = "FAILED"

        private val HTTP_SUCCESS_CODE = arrayOf(
            HttpServletResponse.SC_OK,
            HttpServletResponse.SC_CREATED,
            HttpServletResponse.SC_NOT_MODIFIED,
            HttpServletResponse.SC_MOVED_PERMANENTLY,
            HttpServletResponse.SC_MOVED_TEMPORARILY
        )

        private const val NULL = "[null]"

        private const val SUCCESS = "SUCCESS"

        private const val UNAUTHORIZED = "UNAUTHORIZED"

        private const val VALIDATE_FAILED = "VALIDATE_FAILED"

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
    }
}

@WebFilter(filterName = "traceIdFilter", servletNames = ["dispatcherServlet"])
internal class TraceIdFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) = try {
        MDC.put("X-B3-TraceId", UUID.randomUUID().toString())
        filterChain.doFilter(request, response)
    } finally {
        MDC.remove("X-B3-TraceId")
    }
}

private val jsonFactory = JsonFactory()
internal fun String.getJsonRootValue(field: String): String? {
    jsonFactory.createParser(this).use {
        while (it.nextToken() != null) {
            if (it.currentToken == JsonToken.FIELD_NAME &&
                it.currentName == field &&
                it.parsingContext.parent.inRoot()
            ) {
                it.nextToken()
                return it.valueAsString
            }
        }
    }
    return null
}
