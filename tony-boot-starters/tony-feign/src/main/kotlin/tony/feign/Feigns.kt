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

@file:JvmName("Feigns")

package tony.feign

/**
 * Feign 工具
 */
import com.fasterxml.jackson.databind.JsonNode
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import org.springframework.http.MediaType
import tony.utils.globalObjectMapper
import tony.utils.jsonNode
import tony.utils.md5
import tony.utils.toJsonString

/**
 * request body string
 * @receiver [RequestBody]
 * @return
 */
public fun RequestBody.string(): String =
    Buffer()
        .let { buffer ->
            writeTo(buffer)
            String(buffer.readByteArray())
        }

/**
 * request body 读取成 jackson的 [JsonNode]
 * @return
 */
public fun RequestBody.jsonNode(): JsonNode =
    Buffer()
        .let { buffer ->
            writeTo(buffer)
            buffer
                .readByteArray()
                .jsonNode()
        }

/**
 * Parsed media
 */
@get:JvmSynthetic
internal val ResponseBody.parsedMedia: MediaType?
    get() {
        val contentTypeStr = contentType()?.toString()
        return if (contentTypeStr.isNullOrBlank()) {
            null
        } else {
            MediaType.parseMediaType(contentTypeStr)
        }
    }

/**
 * Parsed media
 */
@get:JvmSynthetic
internal val RequestBody.parsedMedia: MediaType?
    get() {
        val contentTypeStr = contentType()?.toString()
        return if (contentTypeStr.isNullOrBlank()) {
            null
        } else {
            MediaType.parseMediaType(contentTypeStr)
        }
    }

/**
 * 是否字符串mime类型
 * @param mediaType
 */
@JvmSynthetic
internal fun isTextMediaTypes(mediaType: MediaType?) =
    TEXT_MEDIA_TYPES.any { it.includes(mediaType) }

private val TEXT_MEDIA_TYPES =
    listOf(
        MediaType.TEXT_XML,
        MediaType.TEXT_HTML,
        MediaType.TEXT_PLAIN,
        MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_FORM_URLENCODED
    )

/**
 * @see [JsonNode.sortRequestBody]
 * @param timestampStr
 * @return
 */
public fun CharSequence.sortRequestBody(timestampStr: CharSequence): String =
    globalObjectMapper
        .readTree(this.toString())
        .sortRequestBody(timestampStr.toString())

/**
 * 生成简单签名.
 * @param appId
 * @param secret
 * @return
 */
public fun CharSequence.genSign(
    appId: CharSequence,
    secret: CharSequence,
): String =
    ("$appId|$secret|$this".md5().uppercase())
        .md5()
        .uppercase()

/**
 * 请求字段排序. 并将字符串加进请求.
 * @param timestampStr
 * @return
 */
public fun JsonNode.sortRequestBody(timestampStr: CharSequence): String =
    fieldNames()
        .asSequence()
        .sorted()
        .fold<String, LinkedHashMap<String, Any?>>(
            linkedMapOf("timestamp" to timestampStr)
        ) { map, key ->
            if (key != "timestamp") {
                map[key] = this[key]
            }
            map
        }.toJsonString()
