@file:JvmName("FeignUtils")

package com.tony.feign

import com.fasterxml.jackson.databind.JsonNode
import com.tony.utils.OBJECT_MAPPER
import com.tony.utils.toJsonString
import com.tony.utils.toMd5UppercaseString
import okhttp3.RequestBody
import okio.Buffer

fun RequestBody.string() = run {
    val buffer = Buffer()
    writeTo(buffer)
    String(buffer.readByteArray())
}

fun RequestBody.jsonNode(): JsonNode = run {
    val buffer = Buffer()
    writeTo(buffer)
    OBJECT_MAPPER.readTree(buffer.readByteArray())
}

fun String.sortRequestBody(
    timestampStr: String,
): String =
    OBJECT_MAPPER
        .readTree(this)
        .sortRequestBody(timestampStr)

fun String.genSign(appId: String, secret: String) =
    ("$appId|$secret|$this".toMd5UppercaseString()).toMd5UppercaseString()

fun JsonNode.sortRequestBody(
    timestampStr: String,
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
