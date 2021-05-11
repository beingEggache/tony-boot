@file:Suppress("MemberVisibilityCanBePrivate")

package com.tony.webcore

import com.tony.core.ApiResult
import com.tony.core.EMPTY_RESULT
import com.tony.core.exception.BaseException
import com.tony.core.utils.asTo
import com.tony.core.utils.defaultIfBlank
import com.tony.core.utils.doIfNull
import com.tony.webcore.WebApp.apiSession
import com.tony.webcore.utils.headers
import com.tony.webcore.utils.origin
import com.tony.webcore.utils.remoteIp
import com.tony.webcore.utils.url
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.error.ErrorAttributeOptions.Include
import org.springframework.util.ResourceUtils
import org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.context.request.ServletWebRequest
import java.net.URI
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import javax.servlet.http.HttpServletRequest

@Suppress("unused")
object WebContext {

    val current: ServletRequestAttributes
        get() = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes

    fun <T : Any> ServletRequestAttributes.getOrPut(
        key: String,
        scope: Int = SCOPE_REQUEST,
        callback: () -> T
    ) = getAttribute(key, scope).asTo() ?: callback().apply {
        setAttribute(key, this, scope)
    }

    val contextPath: String
        get() = WebApp.contextPath

    val headers: Map<String, String>
        get() = request.headers

    private val errorAttributeOptions = ErrorAttributeOptions.of(Include.MESSAGE)

    internal val error: String
        get() = errorAttributes["error"].asTo() ?: ""

    internal val httpStatus: Int
        get() = errorAttributes["status"] as Int

    private val errorAttributes
        get() = current.getAttribute("errorAttribute", SCOPE_REQUEST)
            .asTo<Map<String, Any?>>().doIfNull {
                val errorAttributes = WebApp
                    .errorAttributes
                    .getErrorAttributes(ServletWebRequest(request), errorAttributeOptions)
                current.setAttribute("errorAttribute", errorAttributes, SCOPE_REQUEST)
                errorAttributes
            }

    val origin: String
        get() = request.origin

    val remoteIP: String
        get() = request.remoteIp

    internal val request: HttpServletRequest
        get() = current.request

    val url: URL
        get() = request.url

    val userId: String
        get() = apiSession.userId

    /**
     * Springboot下获得项目根目录的实际路径
     */
    @Throws(Exception::class)
    fun projectDir(path: String = ""): String {

        tailrec fun getPath(uri: URI): URI {
            val u = URI(uri.schemeSpecificPart)
            return if (u.scheme != null) getPath(u) else u
        }

        val projectClassPath = getPath(ResourceUtils.getURL("classpath:").toURI()).toString()
        val isWindows = System.getProperty("os.name").contains("win", true)
        val substring = if (isWindows) projectClassPath.substring(1) else projectClassPath
        val projectRootPath = Paths.get(substring).parent?.parent?.parent?.toString() ?: ""
        if (path.isNotBlank()) Files.createDirectories(Paths.get(projectRootPath + path))
        return projectRootPath + path
    }

    fun BaseException.toResponse() =
        ApiResult(EMPTY_RESULT, code, message.defaultIfBlank())
}
