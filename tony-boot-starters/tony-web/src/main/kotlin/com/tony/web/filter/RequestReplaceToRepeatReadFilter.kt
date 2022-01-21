package com.tony.web.filter

import com.tony.utils.doIf
import com.tony.web.filter.RepeatReadRequestWrapper.Companion.toRepeatRead
import org.springframework.core.PriorityOrdered
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.Collections
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ReadListener
import javax.servlet.ServletException
import javax.servlet.ServletInputStream
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper
import javax.servlet.http.Part

internal class RequestReplaceToRepeatReadFilter : Filter, PriorityOrdered {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(
        request: ServletRequest?,
        response: ServletResponse?,
        chain: FilterChain
    ) = chain.doFilter(
        if (request is HttpServletRequest)
            request.toRepeatRead()
        else request,
        response
    )

    override fun getOrder() = PriorityOrdered.HIGHEST_PRECEDENCE
}

class RepeatReadRequestWrapper
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

    @Suppress("unused")
    val contentAsByteArray: ByteArray
        get() {
            cachedContent.reset()
            val bytes = cachedContent.readBytes()
            cachedContent.reset()
            return bytes
        }

    override fun getInputStream() =
        object : ServletInputStream() {
            override fun isReady() = true
            override fun setReadListener(listener: ReadListener?) = Unit
            override fun isFinished() = cachedContent.available() == 0
            override fun read() = cachedContent.read()
            override fun reset() = cachedContent.reset()
            override fun markSupported() = cachedContent.markSupported()
        }

    override fun getReader() = cachedContent.bufferedReader(StandardCharsets.UTF_8)

    private fun isFormPost() =
        contentType in formPostContentTypes && HttpMethod.POST.matches(method)

    companion object {

        @JvmStatic
        fun HttpServletRequest.toRepeatRead() =
            if (this is RepeatReadRequestWrapper) this
            else RepeatReadRequestWrapper(this)

        private val formPostContentTypes = arrayOf(
            "application/x-www-form-urlencoded",
            "multipart/form-data"
        )
    }
}
