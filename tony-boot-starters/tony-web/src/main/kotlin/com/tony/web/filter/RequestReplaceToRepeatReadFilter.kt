package com.tony.web.filter

import com.tony.Env
import com.tony.utils.antPathMatchAny
import com.tony.utils.doIf
import com.tony.utils.sanitizedPath
import com.tony.web.WebApp
import com.tony.web.config.WebProperties
import com.tony.web.filter.RepeatReadRequestWrapper.Companion.toRepeatRead
import com.tony.web.utils.isCorsPreflightRequest
import org.springframework.core.PriorityOrdered
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.Collections
import javax.servlet.FilterChain
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.Part

internal class RequestReplaceToRepeatReadFilter(
    private val webProperties: WebProperties
) : OncePerRequestFilter(), PriorityOrdered {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) = filterChain.doFilter(
        request.toRepeatRead(),
        response
    )

    override fun shouldNotFilter(request: HttpServletRequest) =
        request.requestURI.antPathMatchAny(excludedUrls) || request.isCorsPreflightRequest

    override fun getOrder() = PriorityOrdered.HIGHEST_PRECEDENCE

    private val excludedUrls by lazy {
        val contextPath = Env.getProperty("server.servlet.context-path", "")
        webProperties.traceLogExcludePatterns.map { sanitizedPath("$contextPath/$it") }
            .plus(WebApp.whiteUrlPatterns(prefix = contextPath))
    }
}

public class RepeatReadRequestWrapper
@Throws(IOException::class)
internal constructor(request: HttpServletRequest) : HttpServletRequestWrapper(request) {

    /**
     * Don't know why must do this can get parts in controller.
     * 先把parts在初始化时就保存下来，否则会获取不到。暂时的成本低的做法。
     */
    private val initializedParts: MutableCollection<Part> =
        if (request.contentType?.contains(MediaType.MULTIPART_FORM_DATA_VALUE) == true) {
            request.parts
        } else {
            Collections.emptyList()
        }

    override fun getParts(): MutableCollection<Part> = initializedParts

    private val cachedContent =
        ByteArrayOutputStream(request.contentLength.coerceAtLeast(0)).doIf(!isFormPost()) {
            writeBytes(request.inputStream.readBytes())
        }.run {
            ByteArrayInputStream(toByteArray())
        }

    public val contentAsByteArray: ByteArray
        get() {
            cachedContent.reset()
            val bytes = cachedContent.readBytes()
            cachedContent.reset()
            return bytes
        }

    override fun getInputStream(): ServletInputStream =
        object : ServletInputStream() {
            override fun isReady() = true
            override fun setReadListener(listener: ReadListener?) = Unit
            override fun isFinished() = cachedContent.available() == 0
            override fun read() = cachedContent.read()
            override fun reset() = cachedContent.reset()
            override fun markSupported() = cachedContent.markSupported()
        }

    override fun getReader(): BufferedReader = cachedContent.bufferedReader(StandardCharsets.UTF_8)

    private fun isFormPost() =
        contentType in formPostContentTypes && HttpMethod.POST.matches(method)

    internal companion object {

        @JvmStatic
        fun HttpServletRequest.toRepeatRead(): RepeatReadRequestWrapper =
            if (this is RepeatReadRequestWrapper) {
                this
            } else {
                RepeatReadRequestWrapper(this)
            }

        private val formPostContentTypes = arrayOf(
            "application/x-www-form-urlencoded",
            "multipart/form-data"
        )
    }
}
