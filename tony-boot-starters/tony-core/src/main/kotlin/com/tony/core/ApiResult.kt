@file:Suppress("unused")

package com.tony.core

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.tony.core.utils.toJsonString

@JsonPropertyOrder(value = ["code", "msg", "data"])
data class ApiResult<T> @JvmOverloads constructor(
    val data: T?,
    val code: Int,
    val msg: String = ""
) {

    fun toMap() =
        mutableMapOf(
            "code" to code.toString(),
            "data" to data.toJsonString(),
            "msg" to msg
        )
}

val EMPTY_RESULT = EmptyResult()

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
class EmptyResult

data class OneResult<T>(val result: T? = null)

data class ListResult<T> @JvmOverloads constructor(val items: Collection<T>? = listOf())

@JsonPropertyOrder(value = ["page", "size", "total", "pages", "hasNext", "items"])
data class PageResult<T>(
    val items: Collection<T>,
    val page: Long,
    val size: Long,
    val pages: Long,
    val total: Long,
    val hasNext: Boolean
) {
    inline fun <R> map(transform: (T) -> R) = apply {
        items.map(transform)
    }

    inline fun onEach(action: (T) -> Unit) = apply {
        items.onEach(action)
    }

    inline fun firstOrNull(predicate: (T) -> Boolean) = items.firstOrNull(predicate)
}
