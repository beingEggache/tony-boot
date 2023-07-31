package com.tony.web.filter

import com.tony.traceIdHeaderName
import com.tony.utils.antPathMatchAny
import com.tony.utils.mdcPutOrGetDefault
import com.tony.utils.sanitizedPath
import com.tony.utils.toInstant
import com.tony.utils.uuid
import com.tony.web.WebApp
import com.tony.web.config.WebProperties
import com.tony.web.filter.RepeatReadRequestWrapper.Companion.toRepeatRead
import com.tony.web.log.RequestTraceLogger
import com.tony.web.utils.isCorsPreflightRequest
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException
import java.time.LocalDateTime
import org.slf4j.MDC
import org.springframework.core.PriorityOrdered
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingResponseWrapper

/**
 * TraceLoggingFilter
 *
 * @author tangli
 * @since 2023/5/25 10:36
 */
internal class TraceLoggingFilter(
    private val requestTraceLogger: RequestTraceLogger,
    private val webProperties: WebProperties,
) : OncePerRequestFilter(), PriorityOrdered {

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) = doFilterTrace(
        request.toRepeatRead(),
        ContentCachingResponseWrapper(response),
        filterChain,
        LocalDateTime.now()
    )

    @Throws(IOException::class, ServletException::class)
    private fun doFilterTrace(
        request: RepeatReadRequestWrapper,
        response: ContentCachingResponseWrapper,
        chain: FilterChain,
        startTime: LocalDateTime,
    ) = try {
        chain.doFilter(request, response)
    } finally {
        val elapsedTime = System.currentTimeMillis() - startTime.toInstant().toEpochMilli()

        log(request, response, elapsedTime)

        response.copyBodyToResponse()
    }

    private fun log(
        request: RepeatReadRequestWrapper,
        response: ContentCachingResponseWrapper,
        elapsedTime: Long,
    ) = try {
        requestTraceLogger.requestTraceLog(
            request,
            response,
            elapsedTime
        )
    } catch (e: Exception) {
        logger.error(e.message, e)
    }

    override fun shouldNotFilter(request: HttpServletRequest) =
        request.requestURI.antPathMatchAny(excludedUrls) || request.isCorsPreflightRequest

    private val excludedUrls by lazy {
        WebApp
            .responseWrapExcludePatterns
            .plus(webProperties.traceLogExcludePatterns.map { sanitizedPath("${WebApp.contextPath}/$it") })
            .plus(WebApp.whiteUrlPatternsWithContextPath)
    }

    override fun getOrder() = PriorityOrdered.HIGHEST_PRECEDENCE + 2
}

/**
 * TraceIdFilter
 *
 * @author tangli
 * @since 2023/5/25 10:37
 */
internal class TraceIdFilter : OncePerRequestFilter(), PriorityOrdered {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
    ) {
        try {
            val traceId =
                mdcPutOrGetDefault(traceIdHeaderName, uuid()) {
                    request.getHeader(traceIdHeaderName)
                }
            response.setHeader(traceIdHeaderName, traceId)
            chain.doFilter(request, response)
        } finally {
            MDC.remove(traceIdHeaderName)
        }
    }

    override fun getOrder() = PriorityOrdered.HIGHEST_PRECEDENCE
}
