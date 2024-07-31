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

package com.tony.web.filter

/**
 * 跟踪日志过滤器
 * @author tangli
 * @date 2023/09/13 19:48
 * @since 1.0.0
 */
import com.tony.TRACE_ID_HEADER_NAME
import com.tony.utils.antPathMatchAny
import com.tony.utils.getLogger
import com.tony.utils.mdcPutOrGetDefault
import com.tony.utils.sanitizedPath
import com.tony.utils.toInstant
import com.tony.utils.uuid
import com.tony.web.WebContext
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
 * 跟踪日志过滤器
 * @author tangli
 * @date 2023/09/13 19:48
 * @since 1.0.0
 */
internal class TraceLoggingFilter(
    private val requestTraceLogger: RequestTraceLogger,
    /**
     * 请求日志排除url
     */
    traceLogExcludePatterns: List<String>,
) : OncePerRequestFilter(),
    PriorityOrdered {
    private val log = getLogger()

    init {
        log.info("Request trace log is enabled")
        if (traceLogExcludePatterns.isNotEmpty()) {
            log.info("Request trace log exclude patterns: $traceLogExcludePatterns")
        }
    }

    private val excludedUrls by lazy(LazyThreadSafetyMode.PUBLICATION) {
        WebContext
            .responseWrapExcludePatterns
            .plus(traceLogExcludePatterns.map { sanitizedPath("${WebContext.contextPath}/$it") })
            .plus(WebContext.excludePathPatterns(WebContext.contextPath))
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
        val elapsedTime =
            System.currentTimeMillis() -
                startTime
                    .toInstant()
                    .toEpochMilli()

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
        log.error(e.message, e)
    }

    override fun shouldNotFilter(request: HttpServletRequest) =
        request
            .requestURI
            .antPathMatchAny(excludedUrls) ||
            request.isCorsPreflightRequest

    override fun getOrder() =
        PriorityOrdered.HIGHEST_PRECEDENCE + 2
}

/**
 * TraceIdFilter
 *
 * @author tangli
 * @date 2023/05/25 19:37
 */
internal class TraceIdFilter :
    OncePerRequestFilter(),
    PriorityOrdered {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
    ) {
        try {
            val traceId =
                mdcPutOrGetDefault(TRACE_ID_HEADER_NAME, uuid()) {
                    request.getHeader(TRACE_ID_HEADER_NAME)
                }
            response.setHeader(TRACE_ID_HEADER_NAME, traceId)
            chain.doFilter(request, response)
        } finally {
            MDC.remove(TRACE_ID_HEADER_NAME)
        }
    }

    override fun getOrder() =
        PriorityOrdered.HIGHEST_PRECEDENCE
}
