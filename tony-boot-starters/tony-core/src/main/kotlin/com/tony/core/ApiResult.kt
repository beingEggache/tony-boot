@file:Suppress("unused")

package com.tony.core

import com.fasterxml.jackson.annotation.JsonAutoDetect
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
    val size: Long,
    val pages: Long,
    val total: Long,
    val hasNext: Boolean
) : Iterable<T> by items
