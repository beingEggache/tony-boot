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

package tony.web.filter

/**
 * 重复读取请求包装器 相关
 * @author tangli
 * @date 2023/09/28 19:03
 */
import jakarta.servlet.FilterChain
import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.Part
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.Collections
import org.springframework.core.PriorityOrdered
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import tony.core.utils.antPathMatchAny
import tony.core.utils.sanitizedPath
import tony.web.WebContext
import tony.web.filter.RepeatReadRequestWrapper.Companion.toRepeatRead
import tony.web.utils.isCorsPreflightRequest

/**
 * 可重复读请求过滤器.
 * @author tangli
 * @date 2023/09/13 19:47
 */
internal class RequestReplaceToRepeatReadFilter(
    /**
     * 请求日志排除url
     */
    traceLogExcludePatterns: List<String>,
) : OncePerRequestFilter(),
    PriorityOrdered {
    private val excludedUrls by lazy(LazyThreadSafetyMode.PUBLICATION) {
        traceLogExcludePatterns
            .map { sanitizedPath("${WebContext.contextPath}/$it") }
            .plus(WebContext.excludePathPatterns(WebContext.contextPath))
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) =
        filterChain.doFilter(
            request.toRepeatRead(),
            response
        )

    override fun shouldNotFilter(request: HttpServletRequest) =
        request
            .requestURI
            .antPathMatchAny(excludedUrls) ||
            request.isCorsPreflightRequest

    override fun getOrder() =
        PriorityOrdered.HIGHEST_PRECEDENCE + 1
}

/**
 * 可重复读请求包装器.
 * @author tangli
 * @date 2023/09/28 19:03
 */
public class RepeatReadRequestWrapper
    @Throws(IOException::class)
    internal constructor(
        request: HttpServletRequest,
    ) : HttpServletRequestWrapper(request) {
        /**
         * Don't know why must do this can get parts in controller.
         * 先把parts在初始化时就保存下来，否则会获取不到。暂时的成本低的做法。
         */
        private val initializedParts: MutableCollection<Part> =
            if (request
                    .contentType
                    ?.contains(MediaType.MULTIPART_FORM_DATA_VALUE) == true
            ) {
                request.parts
            } else {
                Collections.emptyList()
            }

        override fun getParts(): MutableCollection<Part> =
            initializedParts

        private val cachedContent =
            if (!isFormPost()) {
                request.inputStream.readBytes()
            } else {
                ByteArray(0)
            }

        public val contentAsByteArray: ByteArray
            get() {
                return cachedContent.copyOf()
            }

        override fun getInputStream(): ServletInputStream =
            object : ServletInputStream() {
                private val byteArrayInputStream =
                    ByteArrayInputStream(cachedContent)

                override fun isReady() =
                    true

                override fun setReadListener(listener: ReadListener?) =
                    Unit

                override fun isFinished() =
                    byteArrayInputStream.available() == 0

                override fun read() =
                    byteArrayInputStream.read()

                override fun reset() =
                    byteArrayInputStream.reset()

                override fun markSupported() =
                    byteArrayInputStream.markSupported()
            }

        override fun getReader(): BufferedReader =
            BufferedReader(InputStreamReader(ByteArrayInputStream(cachedContent), StandardCharsets.UTF_8))

        private fun isFormPost() =
            contentType in formPostContentTypes &&
                HttpMethod
                    .POST
                    .matches(method)

        public companion object {
            @JvmStatic
            public fun HttpServletRequest.toRepeatRead(): RepeatReadRequestWrapper =
                this as? RepeatReadRequestWrapper ?: RepeatReadRequestWrapper(this)

            private val formPostContentTypes =
                arrayOf(
                    "application/x-www-form-urlencoded",
                    "multipart/form-data"
                )
        }
    }
