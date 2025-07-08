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
 * Feign 工具方法集合。
 *
 * 适用于 Feign/OkHttp 请求体、响应体的内容读取、签名、参数排序等场景。
 *
 * 注意事项：
 * - string()/jsonNode() 读取大文件/流式 body 时可能导致内存占用高，建议仅用于小型文本 body。
 * - isTextMediaTypes 仅判断常见文本类型，如需支持更多类型可扩展 TEXT_MEDIA_TYPES。
 * - sortRequestBody/genSign 主要用于签名和参数排序，排序规则需与服务端保持一致。
 * - 关键方法如 Buffer 读取、JSON 解析等遇到格式错误会抛出异常，建议业务方捕获处理。
 *
 * @author tangli
 * @date 2023/08/02
 */

import com.fasterxml.jackson.databind.JsonNode
import java.io.IOException
import okhttp3.RequestBody
import okio.Buffer
import org.springframework.http.MediaType
import tony.utils.globalObjectMapper
import tony.utils.jsonNode
import tony.utils.md5
import tony.utils.toJsonString

/**
 * 读取 RequestBody 内容为字符串。
 * 注意：大文件/流式 body 可能导致内存占用高，仅建议用于小型文本 body。
 * @receiver [RequestBody]
 * @return 请求体byteArray
 * @throws IOException 读取失败时抛出
 */
@Throws(IOException::class)
public fun RequestBody.byteArray(): ByteArray =
    Buffer()
        .let { buffer ->
            writeTo(buffer)
            buffer.readByteArray()
        }

/**
 * 读取 RequestBody 内容为字符串。
 * 注意：大文件/流式 body 可能导致内存占用高，仅建议用于小型文本 body。
 * @receiver [RequestBody]
 * @return 请求体字符串内容
 * @throws IOException 读取失败时抛出
 */
@Throws(IOException::class)
public fun RequestBody.string(): String =
    Buffer()
        .let { buffer ->
            writeTo(buffer)
            String(buffer.readByteArray())
        }

/**
 * 读取 RequestBody 内容为 Jackson 的 [JsonNode]。
 * 注意：大文件/流式 body 可能导致内存占用高，仅建议用于小型 JSON body。
 * @return [JsonNode]
 * @throws IOException/JsonParseException 读取或解析失败时抛出
 */
@Throws(IOException::class)
public fun RequestBody.jsonNode(): JsonNode =
    Buffer()
        .let { buffer ->
            writeTo(buffer)
            buffer
                .readByteArray()
                .jsonNode()
        }

/**
 * 解析媒体类型
 * @param [contentType] 内容类型
 * @return [MediaType]?
 * @author tangli
 * @date 2025/07/08 10:38
 */
public fun parseMediaType(contentType: String?): MediaType? =
    if (contentType.isNullOrBlank()) {
        null
    } else {
        MediaType.parseMediaType(contentType)
    }

/**
 * 判断是否为常见文本类型。
 * @param mediaType
 * @return true 表示为常见文本类型
 * 注意：如需支持更多类型可扩展 TEXT_MEDIA_TYPES。
 */
@JvmSynthetic
internal fun isTextMediaTypes(mediaType: MediaType?) =
    TEXT_MEDIA_TYPES.any { it.includes(mediaType) }

// 常见文本类型集合，如需扩展可在此处添加
private val TEXT_MEDIA_TYPES =
    listOf(
        MediaType.TEXT_XML,
        MediaType.TEXT_HTML,
        MediaType.TEXT_PLAIN,
        MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_FORM_URLENCODED
    )

/**
 * 对请求体字符串进行字段排序，并加上 timestamp 字段。
 * 主要用于签名场景，排序规则需与服务端保持一致。
 * @param timestampStr 时间戳字符串
 * @return 排序后的 JSON 字符串
 * @throws Exception 解析或排序失败时抛出
 */
public fun CharSequence.sortRequestBody(timestampStr: CharSequence): String =
    globalObjectMapper
        .readTree(this.toString())
        .sortRequestBody(timestampStr.toString())

/**
 * 生成简单签名。
 * 规则：md5(md5(appId|secret|body)).toUpperCase()
 * @param appId 应用ID
 * @param secret 密钥
 * @return 签名字符串
 */
public fun CharSequence.genSign(
    appId: CharSequence,
    secret: CharSequence,
): String =
    ("$appId|$secret|$this".md5().uppercase())
        .md5()
        .uppercase()

/**
 * 对 JsonNode 字段排序，并加上 timestamp 字段。
 * 主要用于签名场景，排序规则需与服务端保持一致。
 * @param timestampStr 时间戳字符串
 * @return 排序后的 JSON 字符串
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
