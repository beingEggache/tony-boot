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
 * 重复读取请求包装器 相关
 * @author Tang Li
 * @date 2023/09/28 11:03
 * @since 1.0.0
 */
import com.tony.utils.antPathMatchAny
import com.tony.utils.doIf
import com.tony.utils.sanitizedPath
import com.tony.web.WebApp
import com.tony.web.filter.RepeatReadRequestWrapper.Companion.toRepeatRead
import com.tony.web.utils.isCorsPreflightRequest
import jakarta.servlet.FilterChain
import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.Part
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.Collections
import org.springframework.core.PriorityOrdered
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter

/**
 * 将 请求 替换为 可重复读 请求.
 * @author Tang Li
 * @date 2023/09/13 10:47
 * @since 1.0.0
 */
internal class RequestReplaceToRepeatReadFilter(
    traceLogExcludePatterns: List<String>,
) : OncePerRequestFilter(), PriorityOrdered {
    private val excludedUrls by lazy(LazyThreadSafetyMode.PUBLICATION) {
        traceLogExcludePatterns.map { sanitizedPath("${WebApp.contextPath}/$it") }
            .plus(WebApp.whiteUrlPatternsWithContextPath)
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) = filterChain.doFilter(
        request.toRepeatRead(),
        response
    )

    override fun shouldNotFilter(request: HttpServletRequest) =
        request.requestURI.antPathMatchAny(excludedUrls) || request.isCorsPreflightRequest

    override fun getOrder() = PriorityOrdered.HIGHEST_PRECEDENCE + 1
}

/**
 * 重复读取请求包装器
 * @author Tang Li
 * @date 2023/09/28 11:03
 * @since 1.0.0
 */
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

    override fun getInputStream(): ServletInputStream = object : ServletInputStream() {
        override fun isReady() = true

        override fun setReadListener(listener: ReadListener?) = Unit

        override fun isFinished() = cachedContent.available() == 0

        override fun read() = cachedContent.read()

        override fun reset() = cachedContent.reset()

        override fun markSupported() = cachedContent.markSupported()
    }

    override fun getReader(): BufferedReader = cachedContent.bufferedReader(StandardCharsets.UTF_8)

    private fun isFormPost() = contentType in formPostContentTypes && HttpMethod.POST.matches(method)

    public companion object {
        @JvmStatic
        public fun HttpServletRequest.toRepeatRead(): RepeatReadRequestWrapper = if (this is RepeatReadRequestWrapper) {
            this
        } else {
            RepeatReadRequestWrapper(this)
        }

        private val formPostContentTypes =
            arrayOf(
                "application/x-www-form-urlencoded",
                "multipart/form-data"
            )
    }
}
