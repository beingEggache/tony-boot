package com.tony.web.filter

import com.tony.utils.antPathMatchAny
import com.tony.utils.toInstant
import com.tony.utils.toString
import com.tony.utils.uuid
import com.tony.web.WebApp
import com.tony.web.log.RequestTraceLogger
import com.tony.web.utils.isCorsPreflightRequest
import org.slf4j.MDC
import org.springframework.core.PriorityOrdered
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.IOException
import java.time.LocalDateTime
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

internal class TraceLoggingFilter(
    private val requestTraceLogger: RequestTraceLogger
) : OncePerRequestFilter(), PriorityOrdered {

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

        val elapsedTime = System.currentTimeMillis() - startTime.toInstant().toEpochMilli()

        log(request, response, startTime, elapsedTime)

        response.copyBodyToResponse()
    }

    private fun log(
        request: ContentCachingRequestWrapper,
        response: ContentCachingResponseWrapper,
        startTime: LocalDateTime,
        elapsedTime: Long
    ) = try {
        val startTimeStr = startTime.toString("yyyy-MM-dd HH:mm:ss.SSS")
        requestTraceLogger.requestTraceLog(
            request,
            response,
            startTimeStr,
            elapsedTime
        )
    } catch (e: Exception) {
        logger.error(e.message, e)
    }

    override fun shouldNotFilter(request: HttpServletRequest) =
        request.requestURI.antPathMatchAny(excludedUrls) || request.isCorsPreflightRequest

    private val excludedUrls by lazy {
        WebApp.responseWrapExcludePatterns.plus(WebApp.ignoreUrlPatterns())
    }

    override fun getOrder() = PriorityOrdered.HIGHEST_PRECEDENCE + 1
}

internal class TraceIdFilter : OncePerRequestFilter(), PriorityOrdered {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) = try {
        MDC.put("X-B3-TraceId", uuid())
        chain.doFilter(request, response)
    } finally {
        MDC.remove("X-B3-TraceId")
    }

    override fun getOrder() = PriorityOrdered.HIGHEST_PRECEDENCE
}
