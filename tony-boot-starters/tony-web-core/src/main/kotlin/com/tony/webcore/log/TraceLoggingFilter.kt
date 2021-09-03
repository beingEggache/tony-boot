package com.tony.webcore.log

import com.tony.core.utils.toInstant
import com.tony.core.utils.toString
import com.tony.webcore.WebApp
import com.tony.webcore.utils.antPathMatcher
import com.tony.webcore.utils.isCorsPreflightRequest
import com.tony.webcore.utils.matchAny
import org.slf4j.MDC
import org.springframework.core.PriorityOrdered
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
    private val requestTraceLogger: RequestTraceLogger
) : OncePerRequestFilter() {

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
        antPathMatcher.matchAny(excludedUrls, request.requestURI) || request.isCorsPreflightRequest

    private val excludedUrls by lazy {
        WebApp.excludeJsonResultUrlPatterns.plus(WebApp.ignoreUrlPatterns())
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
