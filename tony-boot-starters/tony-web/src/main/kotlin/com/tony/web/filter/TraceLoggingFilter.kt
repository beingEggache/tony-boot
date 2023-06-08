package com.tony.web.filter

import com.tony.utils.antPathMatchAny
import com.tony.utils.defaultIfBlank
import com.tony.utils.sanitizedPath
import com.tony.utils.toInstant
import com.tony.utils.uuid
import com.tony.web.WebApp
import com.tony.web.config.WebProperties
import com.tony.web.filter.RepeatReadRequestWrapper.Companion.toRepeatRead
import com.tony.web.log.RequestTraceLogger
import com.tony.web.utils.isCorsPreflightRequest
import org.slf4j.MDC
import org.springframework.core.PriorityOrdered
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.IOException
import java.time.LocalDateTime
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

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

    init {
        logger.info("Request trace log is enabled.")
        if (webProperties.traceLoggerEnabled && webProperties.traceLogExcludePatterns.isNotEmpty()) {
            logger.info("Request trace log exclude patterns: ${webProperties.traceLogExcludePatterns}")
        }
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) = doFilterTrace(
        request.toRepeatRead(),
        ContentCachingResponseWrapper(response),
        filterChain,
        LocalDateTime.now(),
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
            elapsedTime,
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

    override fun getOrder() = PriorityOrdered.HIGHEST_PRECEDENCE + 1
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
        val traceIdKey = "X-B3-TraceId"
        try {
            val traceId = request.getHeader(traceIdKey).defaultIfBlank(uuid())
            MDC.put(traceIdKey, traceId)
            response.setHeader(traceIdKey, traceId)
            chain.doFilter(request, response)
        } finally {
            MDC.remove(traceIdKey)
        }
    }

    override fun getOrder() = PriorityOrdered.HIGHEST_PRECEDENCE
}
