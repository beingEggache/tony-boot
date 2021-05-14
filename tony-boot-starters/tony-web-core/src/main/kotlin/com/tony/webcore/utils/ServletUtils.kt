@file:JvmName("ServletUtils")
@file:Suppress("unused")

package com.tony.webcore.utils

import com.tony.core.utils.doIf
import com.tony.webcore.WebContext
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import java.net.URL
import java.net.URLEncoder
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

val HttpServletRequest.origin
    get() = run {
        val protocol = url.protocol
        val host = url.host
        val port = url.port
        val contextPath = WebContext.request.contextPath
        "$protocol://$host${if (port == 80 || port < 0) "" else ":$port"}$contextPath"
    }

val HttpServletRequest.headers: Map<String, String>
    get() = headerNames
        .asSequence()
        .associateWith {
            getHeaders(it).toList().joinToString(",")
        }

val HttpServletRequest.remoteIp: String
    get() {
        getHeader("X-Real-IP")?.run {
            if (isNotBlank() && !"unknown".equals(this, true))
                return this
        }
        getHeader("X-Forwarded-For")?.run {
            if (isNotBlank() && !"unknown".equals(this, true))
                return this
        }

        getHeader("ip")?.run {
            if (isNotBlank() && !"unknown".equals(this, true))
                return this
        }

        remoteAddr.takeIf { it.isNotBlank() && it.contains(",") }
            ?.run {
                return substring(0, indexOf(",")).trim { it <= ' ' }
            }

        return remoteAddr
    }

val HttpServletRequest.url
    get() = URL(requestURL.toString())

private val TEXT_MEDIA_TYPES = listOf(
    MediaType.TEXT_XML,
    MediaType.TEXT_HTML,
    MediaType.TEXT_PLAIN,
    MediaType.APPLICATION_JSON
)

val HttpServletResponse.parsedMedia: MediaType?
    get() = if (contentType.isNullOrBlank()) null
    else MediaType.parseMediaType(contentType)

val HttpServletRequest.parsedMedia: MediaType?
    get() = if (contentType.isNullOrBlank()) null
    else MediaType.parseMediaType(contentType)

fun isTextMediaTypes(mediaType: MediaType?) =
    TEXT_MEDIA_TYPES.any { it.includes(mediaType) }

val HttpServletRequest.isCorsPreflightRequest
    get() = method.equals(HttpMethod.OPTIONS.name, true) &&
        !getHeader("Origin").isNullOrBlank() &&
        !getHeader("Access-Control-Request-Method").isNullOrBlank()

@Suppress("unused")
fun ByteArray.responseEntity(
    fileName: String = "",
    contentType: MediaType = MediaType.APPLICATION_OCTET_STREAM
): ResponseEntity<ByteArray> =
    ResponseEntity
        .ok()
        .contentType(contentType)
        .doIf(MediaType.APPLICATION_OCTET_STREAM == contentType) {
            val httpHeaders = HttpHeaders().apply {
                contentDisposition = ContentDisposition
                    .builder("attachment")
                    .filename(URLEncoder.encode(fileName, "utf-8"))
                    .build()
            }
            headers(httpHeaders)
        }
        .body(this)
