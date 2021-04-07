package com.tony.webcore.filter

import com.tony.core.utils.doIf
import org.springframework.core.PriorityOrdered
import org.springframework.http.HttpMethod
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import javax.annotation.Priority
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ReadListener
import javax.servlet.ServletException
import javax.servlet.ServletInputStream
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

@WebFilter(filterName = "httpServletRequestReplacedFilter", servletNames = ["dispatcherServlet"])
@Priority(PriorityOrdered.HIGHEST_PRECEDENCE + 102)
class HttpServletRequestReplacedFilter : Filter {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(
        request: ServletRequest?,
        response: ServletResponse?,
        chain: FilterChain
    ) = chain.doFilter(
        if (request is HttpServletRequest)
            RepeatReadRequestWrapper(request)
        else request,
        response
    )
}

class RepeatReadRequestWrapper(request: HttpServletRequest) : HttpServletRequestWrapper(request) {

    private val cachedContent =
        ByteArrayOutputStream(
            if (request.contentLength >= 0) request.contentLength else 1024
        ).apply {
            (request.contentLength > 0 && !isFormPost()).doIf {
                val bytes: ByteArray = request.inputStream.readBytes()
                bytes.isNotEmpty().doIf { writeBytes(bytes) }
            }
        }

    private val inputStream = ByteArrayInputStream(cachedContent.toByteArray())

    @Throws(IOException::class)
    override fun getInputStream() =
        object : ServletInputStream() {
            override fun isFinished() = true
            override fun isReady() = true
            override fun setReadListener(listener: ReadListener?) = Unit

            @Throws(IOException::class)
            override fun read() = inputStream.read()
        }

    @Throws(IOException::class)
    override fun getReader() =
        BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8))

    private fun isFormPost() =
        contentType in formPostContentTypes && HttpMethod.POST.matches(method)

    companion object {
        private val formPostContentTypes = arrayOf(
            "application/x-www-form-urlencoded",
            "multipart/form-data"
        )
    }
}
