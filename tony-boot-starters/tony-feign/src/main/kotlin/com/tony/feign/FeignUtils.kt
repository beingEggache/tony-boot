@file:JvmName("FeignUtils")

package com.tony.feign

import com.fasterxml.jackson.databind.JsonNode
import com.tony.utils.OBJECT_MAPPER
import com.tony.utils.md5Uppercase
import com.tony.utils.toJsonString
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import org.springframework.http.MediaType

public fun RequestBody.string(): String = run {
    val buffer = Buffer()
    writeTo(buffer)
    String(buffer.readByteArray())
}

public fun RequestBody.jsonNode(): JsonNode = run {
    val buffer = Buffer()
    writeTo(buffer)
    OBJECT_MAPPER.readTree(buffer.readByteArray())
}

internal val ResponseBody.parsedMedia: MediaType?
    get() {
        val contentTypeStr = contentType()?.toString()
        return if (contentTypeStr.isNullOrBlank()) {
            null
        } else {
            MediaType.parseMediaType(contentTypeStr)
        }
    }

internal val RequestBody.parsedMedia: MediaType?
    get() {
        val contentTypeStr = contentType()?.toString()
        return if (contentTypeStr.isNullOrBlank()) {
            null
        } else {
            MediaType.parseMediaType(contentTypeStr)
        }
    }

internal fun isTextMediaTypes(mediaType: MediaType?) =
    TEXT_MEDIA_TYPES.any { it.includes(mediaType) }

private val TEXT_MEDIA_TYPES = listOf(
    MediaType.TEXT_XML,
    MediaType.TEXT_HTML,
    MediaType.TEXT_PLAIN,
    MediaType.APPLICATION_JSON,
    MediaType.APPLICATION_FORM_URLENCODED
)

public fun String.sortRequestBody(
    timestampStr: String
): String =
    OBJECT_MAPPER
        .readTree(this)
        .sortRequestBody(timestampStr)

public fun String.genSign(appId: String, secret: String): String =
    ("$appId|$secret|$this".md5Uppercase()).md5Uppercase()

public fun JsonNode.sortRequestBody(
    timestampStr: String
): String =
    fieldNames()
        .asSequence()
        .sorted()
        .fold<String, LinkedHashMap<String, Any?>>(
            linkedMapOf("timestamp" to timestampStr)
        ) { map, key ->
            if (key == "timestamp") {
                return@fold map
            }
            map[key] = this[key]
            map
        }.toJsonString()
