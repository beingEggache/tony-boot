@file:Suppress("unused")

package com.tony.core

import com.tony.core.utils.toJsonString

data class ApiResult<T> @JvmOverloads constructor(
    val data: T?,
    val code: Int,
    val msg: String = "") {

    fun toMap() =
        mutableMapOf(
            "code" to code.toString(),
            "data" to data.toJsonString(),
            "msg" to msg)
}

data class OneResult<T>(val result: T? = null)

data class ListResult<T> @JvmOverloads constructor(val items: Collection<T>? = listOf())

data class PageResult<T>(
    val items: Collection<T>,
    val page: Long,
    val size: Long,
    val pages: Long,
    val total: Long,
    val hasNext: Boolean) {

    fun <E> map(transformer: (T) -> E) =
        PageResult(
            items.map(transformer), page, size, pages, total, hasNext
        )

}
