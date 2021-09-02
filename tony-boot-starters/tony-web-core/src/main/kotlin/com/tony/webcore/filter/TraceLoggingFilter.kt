package com.tony.webcore.filter

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonToken
import com.tony.core.ApiCode
import com.tony.core.utils.defaultIfBlank
import com.tony.core.utils.defaultZoneOffset
import com.tony.core.utils.getLogger
import com.tony.core.utils.removeLineBreak
import com.tony.core.utils.toJsonString
import com.tony.core.utils.toString
import com.tony.webcore.WebApp
import com.tony.webcore.WebContext
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
internal class TraceLoggingFilter : OncePerRequestFilter() {

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
        request: ContentCachingRequestWrapper,
        response: ContentCachingResponseWrapper,
        chain: FilterChain,
        startTime: LocalDateTime
    ) = try {
        chain.doFilter(request, response)
    } finally {
        val elapsedTime = System.currentTimeMillis() - startTime.toInstant(defaultZoneOffset).toEpochMilli()

        log(request, response, startTime, elapsedTime)

        response.copyBodyToResponse()
    }

    private fun log(
        request: ContentCachingRequestWrapper,
        response: ContentCachingResponseWrapper,
        startTime: LocalDateTime,
        elapsedTime: Long
    ) = try {

        val url = request.requestURL?.toString()
        val path = request.requestURI.removePrefix(WebContext.contextPath)
        val httpMethod = request.method
        val remoteIp = request.remoteIp
        val localIp = request.localAddr
        val query = request.queryString ?: NULL
        val requestParam = requestParam(request)
        val headers = request.headers.toJsonString()
        val responseBody = responseBody(response)
        val resultCode = resultCode(responseBody.defaultIfBlank(), response.status)
        val resultStatus = when (resultCode) {
            ApiCode.successCode -> SUCCESS
            ApiCode.validationErrorCode -> VALIDATE_FAILED
            ApiCode.bizErrorCode -> BIZ_FAILED
            ApiCode.unauthorizedCode -> UNAUTHORIZED
            in 400 * 100..499 * 100 -> VALIDATE_FAILED
            else -> FAILED
        }
        val protocol = request.protocol
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

    override fun shouldNotFilter(request: HttpServletRequest) =
        antPathMatcher.matchAny(EXCLUDE_URLS, request.requestURI) || request.isCorsPreflightRequest

    private companion object {

        private const val SUCCESS = "SUCCESS"

        private const val FAILED = "FAILED"

        private const val BIZ_FAILED = "BIZ_FAILED"

        private const val VALIDATE_FAILED = "VALIDATE_FAILED"

        private const val UNAUTHORIZED = "UNAUTHORIZED"

        private const val NULL = "[null]"

        private val EXCLUDE_URLS by lazy {
            WebApp.excludeJsonResultUrlPatterns.plus(WebApp.ignoreUrlPatterns())
        }

        private val HTTP_SUCCESS_CODE = arrayOf(
            HttpServletResponse.SC_OK,
            HttpServletResponse.SC_CREATED,
            HttpServletResponse.SC_NOT_MODIFIED,
            HttpServletResponse.SC_MOVED_PERMANENTLY,
            HttpServletResponse.SC_MOVED_TEMPORARILY
        )

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

        private fun resultCode(responseBody: String, status: Int) = let {
            val codeFromResponseDirectly = responseBody.codeFromResponseDirectly("code")
            when {
                codeFromResponseDirectly != null -> codeFromResponseDirectly
                HTTP_SUCCESS_CODE.contains(status) -> ApiCode.successCode
                else -> ApiCode.errorCode
            }
        }

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
    }
}

@WebFilter(filterName = "traceIdFilter", servletNames = ["dispatcherServlet"])
internal class TraceIdFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) = try {
        MDC.put("X-B3-TraceId", UUID.randomUUID().toString())
        chain.doFilter(request, response)
    } finally {
        MDC.remove("X-B3-TraceId")
    }
}
