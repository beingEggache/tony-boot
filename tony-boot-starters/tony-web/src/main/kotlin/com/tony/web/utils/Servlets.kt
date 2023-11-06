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

package com.tony.web.utils

/**
 * ServletUtils
 *
 * @author Tang Li
 * @date 2023/5/25 10:42
 */
import com.tony.utils.applyIf
import com.tony.web.WebContext
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.net.URL
import java.net.URLEncoder
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.cors.CorsUtils

/**
 * 获取请求根路径, 包含 [HttpServletRequest.getContextPath].
 *
 * 类似 http://www.whatever.com:8080/context-path.
 *
 * 当端口号为80或443时省略.
 *
 * @receiver [HttpServletRequest]
 */
public val HttpServletRequest.origin: String
    get() =
        run {
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
 * 请求头
 * @receiver [HttpServletRequest]
 */
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
 * 响应头
 * @receiver [HttpServletRequest]
 */
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
 * 获取请求ip.
 *
 * 针对反向代理的情况, 会依次从 X-Real-IP, X-Forwarded-For, ip 中获取.
 * @receiver [HttpServletRequest]
 */
public val HttpServletRequest.remoteIp: String
    get() {
        getHeader("X-Real-IP")?.run {
            if (isNotBlank() && !"unknown".equals(this, true)) {
                return this
            }
        }
        getHeader("X-Forwarded-For")?.run {
            if (isNotBlank() && !"unknown".equals(this, true)) {
                return this
            }
        }

        getHeader("ip")?.run {
            if (isNotBlank() && !"unknown".equals(this, true)) {
                return this
            }
        }

        remoteAddr
            .takeIf { it.isNotBlank() && it.contains(",") }
            ?.run {
                return substring(0, indexOf(",")).trim { it <= ' ' }
            }

        return remoteAddr
    }

/**
 * Url
 * @receiver [HttpServletRequest]
 */
public val HttpServletRequest.url: URL
    get() =
        URL(requestURL.toString())

private val TEXT_MEDIA_TYPES =
    listOf(
        MediaType.TEXT_XML,
        MediaType.TEXT_HTML,
        MediaType.TEXT_PLAIN,
        MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_FORM_URLENCODED
    )

/**
 * 是否字符串mime类型
 * @param mediaType
 * @return
 */
public fun isTextMediaTypes(mediaType: MediaType?): Boolean =
    TEXT_MEDIA_TYPES.any { it.includes(mediaType) }

/**
 * Is cors request a preflight request.
 * @receiver [HttpServletRequest]
 */
public val HttpServletRequest.isCorsPreflightRequest: Boolean
    get() =
        CorsUtils.isPreFlightRequest(this)

/**
 * Parsed media
 * @receiver [HttpServletRequest]
 */
public val HttpServletRequest.parsedMedia: MediaType?
    get() =
        if (contentType.isNullOrBlank()) {
            null
        } else {
            MediaType.parseMediaType(contentType)
        }

/**
 * Parsed media
 * @receiver [HttpServletResponse]
 */
public val HttpServletResponse.parsedMedia: MediaType?
    get() =
        if (contentType.isNullOrBlank()) {
            null
        } else {
            MediaType.parseMediaType(contentType)
        }

/**
 * Status is 1xx informational
 * @receiver [HttpServletResponse]
 */
public val HttpServletResponse.status1xxInformational: Boolean
    get() =
        HttpStatus
            .valueOf(status)
            .is1xxInformational

/**
 * Status is 2xx successful
 * @receiver [HttpServletResponse]
 */
public val HttpServletResponse.status2xxSuccessful: Boolean
    get() =
        HttpStatus
            .valueOf(status)
            .is2xxSuccessful

/**
 * Status is 3xx redirection
 * @receiver [HttpServletResponse]
 */
public val HttpServletResponse.status3xxRedirection: Boolean
    get() =
        HttpStatus
            .valueOf(status)
            .is3xxRedirection

/**
 * 将二进制转为web响应
 *
 * @receiver [ByteArray]
 * @param fileName   文件名
 * @param contentType mime类型
 * @return
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
