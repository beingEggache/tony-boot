@file:Suppress("unused")

package com.tony.core

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.tony.core.utils.toJsonString

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

data class PageResult<T>(
    val items: Collection<T>,
    val page: Long,
    @JsonProperty("size")
    val sizes: Long,
    val pages: Long,
    val total: Long,
    val hasNext: Boolean
) : Collection<T> {

    @get:JsonIgnore
    override val size: Int
        get() = items.size

    inline fun <R> map(transform: (T) -> R) =
        PageResult(items.map(transform), page, sizes, pages, total, hasNext)

    override fun contains(element: T): Boolean = items.contains(element)

    override fun containsAll(elements: Collection<T>): Boolean = items.containsAll(elements)

    override fun isEmpty() = items.isEmpty()

    override fun iterator() = items.iterator()
}
