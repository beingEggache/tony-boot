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

@file:JvmName("Servlets")

package tony.web.utils

/**
 * ServletUtils
 *
 * @author tangli
 * @date 2023/05/25 19:42
 */
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.net.URI
import java.net.URL
import java.net.URLEncoder
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.cors.CorsUtils
import tony.utils.applyIf
import tony.utils.ifNull
import tony.web.WebContext

/**
 * 获取请求根路径，包含 [HttpServletRequest.getContextPath]。
 *
 * 例如：http://www.whatever.com:8080/context-path。
 *
 * 端口号为80或443时省略。
 *
 * @receiver [HttpServletRequest] 当前请求对象
 * @return [String] 请求根路径
 * @author tangli
 * @date 2023/05/25 19:42
 */
@get:JvmSynthetic
@get:JvmName("origin")
public val HttpServletRequest.origin: String
    get() =
        run {
            val url = url
            val protocol = url.protocol
            val host = url.host
            val port = url.port
            val contextPath =
                WebContext
                    .request
                    .contextPath
            "$protocol://$host${if (port == 80 || port == 443 || port < 0) "" else ":$port"}$contextPath"
        }

/**
 * 获取请求根路径，包含 [HttpServletRequest.getContextPath]。
 *
 * 例如：http://www.whatever.com:8080/context-path。
 *
 * 端口号为80或443时省略。
 *
 * @return [String] 请求根路径
 * @author tangli
 * @date 2024/02/06 15:10
 */
public fun origin(): String =
    WebContext.request.origin

/**
 * 获取所有请求头，返回Map结构。
 *
 * @receiver [HttpServletRequest] 当前请求对象
 * @return [Map]<[String], [String]> 请求头键值对，若同名header有多个值则用逗号拼接
 * @author tangli
 * @date 2024/02/06 15:11
 */
@get:JvmSynthetic
@get:JvmName("headers")
public val HttpServletRequest.headers: Map<String, String>
    get() =
        headerNames
            .asSequence()
            .associateWith {
                getHeaders(it)
                    .toList()
                    .joinToString(",")
            }

/**
 * 获取所有请求头，返回Map结构。
 *
 * @return [Map]<[String], [String]> 请求头键值对
 * @author tangli
 * @date 2024/02/06 15:11
 */
public fun requestHeaders(): Map<String, String> =
    WebContext.request.headers

/**
 * 获取所有响应头，返回Map结构。
 *
 * @receiver [HttpServletResponse] 当前响应对象
 * @return [Map]<[String], [String]> 响应头键值对，若同名header有多个值则用逗号拼接
 * @author tangli
 * @date 2024/02/06 15:11
 */
@get:JvmSynthetic
@get:JvmName("headers")
public val HttpServletResponse.headers: Map<String, String>
    get() =
        headerNames
            .asSequence()
            .associateWith {
                getHeaders(it)
                    .toList()
                    .joinToString(",")
            }

/**
 * 获取所有响应头，返回Map结构。
 *
 * @return [Map]<[String], [String]> 响应头键值对
 * @author tangli
 * @date 2024/02/06 15:11
 */
public fun responseHeaders(): Map<String, String> =
    WebContext.response?.headers.ifNull(mapOf())

/**
 * 获取请求IP地址。
 *
 * 优先级依次为 X-Real-IP、X-Forwarded-For、ip header，最后取 remoteAddr。
 * 适用于反向代理、负载均衡等多层代理场景。
 *
 * @receiver [HttpServletRequest] 当前请求对象
 * @return [String] 客户端真实IP
 * @note 若 X-Forwarded-For 有多个IP，仅取第一个。
 * @author tangli
 * @date 2024/02/06 15:14
 */
@get:JvmSynthetic
@get:JvmName("remoteIp")
public val HttpServletRequest.remoteIp: String
    get() =
        sequenceOf(
            getHeader("X-Real-IP"),
            getHeader("X-Forwarded-For")?.split(",")?.firstOrNull(),
            getHeader("ip"),
            remoteAddr?.split(",")?.firstOrNull()
        ).map { it?.trim() }
            .firstOrNull { !it.isNullOrBlank() && !it.equals("unknown", ignoreCase = true) }
            ?: remoteAddr.orEmpty()

/**
 * 获取请求IP地址。
 *
 * 优先级依次为 X-Real-IP、X-Forwarded-For、ip header，最后取 remoteAddr。
 * 适用于反向代理、负载均衡等多层代理场景。
 *
 * @return [String] 客户端真实IP
 * @author tangli
 * @date 2024/02/06 15:14
 */
public fun remoteIp(): String =
    WebContext.request.remoteIp

/**
 * 获取请求完整URL。
 *
 * @receiver [HttpServletRequest] 当前请求对象
 * @return [URL] 请求的完整URL
 * @author tangli
 * @date 2024/02/06 15:14
 */
@get:JvmSynthetic
@get:JvmName("url")
public val HttpServletRequest.url: URL
    get() = URI(requestURL.toString()).toURL()

/**
 * 获取请求完整URL。
 *
 * @return [URL] 请求的完整URL
 * @author tangli
 * @date 2024/02/06 15:14
 */
public fun url(): URL =
    WebContext.request.url

private val TEXT_MEDIA_TYPES =
    listOf(
        MediaType.TEXT_XML,
        MediaType.TEXT_HTML,
        MediaType.TEXT_PLAIN,
        MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_FORM_URLENCODED
    )

/**
 * 判断给定的MediaType是否为常见文本类型（如json、xml、html、plain等）。
 *
 * @param mediaType 需要判断的媒体类型
 * @return [Boolean] 是否为文本类型
 * @note 可根据实际需求扩展 TEXT_MEDIA_TYPES
 * @author tangli
 * @date 2024/02/06 15:15
 */
public fun isTextMediaTypes(mediaType: MediaType?): Boolean =
    TEXT_MEDIA_TYPES.any { it.includes(mediaType) }

/**
 * 判断当前请求是否为CORS预检请求（preflight）。
 *
 * @receiver [HttpServletRequest] 当前请求对象
 * @return [Boolean] 是否为预检请求
 * @author tangli
 * @date 2024/02/06 15:15
 */
@get:JvmSynthetic
@get:JvmName("isCorsPreflightRequest")
public val HttpServletRequest.isCorsPreflightRequest: Boolean
    get() =
        CorsUtils.isPreFlightRequest(this)

/**
 * 判断当前请求是否为CORS预检请求（preflight）。
 *
 * @return [Boolean] 是否为预检请求
 * @author tangli
 * @date 2024/02/06 15:15
 */
public fun isCorsPreflightRequest(): Boolean =
    WebContext.request.isCorsPreflightRequest

/**
 * 获取请求的Content-Type并解析为MediaType。
 *
 * @receiver [HttpServletRequest] 当前请求对象
 * @return [MediaType]? 解析后的媒体类型，若无则为null
 * @author tangli
 * @date 2024/02/06 15:37
 */
@get:JvmSynthetic
@get:JvmName("parsedMedia")
public val HttpServletRequest.parsedMedia: MediaType?
    get() =
        if (contentType.isNullOrBlank()) {
            null
        } else {
            MediaType.parseMediaType(contentType)
        }

/**
 * 获取请求的Content-Type并解析为MediaType。
 *
 * @return [MediaType]? 解析后的媒体类型，若无则为null
 * @author tangli
 * @date 2024/02/06 15:37
 */
public fun requestParsedMedia(): MediaType? =
    WebContext.request.parsedMedia

/**
 * 获取响应的Content-Type并解析为MediaType。
 *
 * @receiver [HttpServletResponse] 当前响应对象
 * @return [MediaType]? 解析后的媒体类型，若无则为null
 * @author tangli
 * @date 2024/02/06 15:37
 */
@get:JvmSynthetic
@get:JvmName("parsedMedia")
public val HttpServletResponse.parsedMedia: MediaType?
    get() =
        if (contentType.isNullOrBlank()) {
            null
        } else {
            MediaType.parseMediaType(contentType)
        }

/**
 * 获取响应的Content-Type并解析为MediaType。
 *
 * @return [MediaType]? 解析后的媒体类型，若无则为null
 * @author tangli
 * @date 2024/02/06 15:37
 */
public fun responseParsedMedia(): MediaType? =
    WebContext.response?.parsedMedia

/**
 * 判断响应状态码是否为1xx（信息性响应）。
 *
 * @receiver [HttpServletResponse] 当前响应对象
 * @return [Boolean] 是否为1xx状态
 * @author tangli
 * @date 2024/02/06 15:38
 */
@get:JvmSynthetic
@get:JvmName("status1xxInformational")
public val HttpServletResponse.status1xxInformational: Boolean
    get() =
        HttpStatus
            .valueOf(status)
            .is1xxInformational

/**
 * 判断响应状态码是否为2xx（成功响应）。
 *
 * @receiver [HttpServletResponse] 当前响应对象
 * @return [Boolean] 是否为2xx状态
 * @author tangli
 * @date 2024/02/06 15:38
 */
@get:JvmSynthetic
@get:JvmName("status2xxSuccessful")
public val HttpServletResponse.status2xxSuccessful: Boolean
    get() =
        HttpStatus
            .valueOf(status)
            .is2xxSuccessful

/**
 * 判断响应状态码是否为3xx（重定向响应）。
 *
 * @receiver [HttpServletResponse] 当前响应对象
 * @return [Boolean] 是否为3xx状态
 * @author tangli
 * @date 2024/02/06 15:38
 */
@get:JvmSynthetic
@get:JvmName("status3xxRedirection")
public val HttpServletResponse.status3xxRedirection: Boolean
    get() =
        HttpStatus
            .valueOf(status)
            .is3xxRedirection

/**
 * 将二进制数据包装为Web响应，常用于文件下载。
 *
 * @receiver [ByteArray] 文件内容
 * @param fileName 文件名，默认为空
 * @param contentType 响应的媒体类型，默认为 application/octet-stream
 * @return [ResponseEntity]<[ByteArray]> Spring Web响应对象
 * @note contentType为octet-stream时自动设置为附件下载
 * @author tangli
 * @date 2024/02/06 15:39
 */
public fun ByteArray.responseEntity(
    fileName: String = "",
    contentType: MediaType = MediaType.APPLICATION_OCTET_STREAM,
): ResponseEntity<ByteArray> =
    ResponseEntity
        .ok()
        .contentType(contentType)
        .applyIf(MediaType.APPLICATION_OCTET_STREAM == contentType) {
            val httpHeaders =
                HttpHeaders().apply {
                    contentDisposition =
                        ContentDisposition
                            .builder("attachment")
                            .filename(URLEncoder.encode(fileName, "utf-8"))
                            .build()
                }
            headers(httpHeaders)
        }.body(this)
