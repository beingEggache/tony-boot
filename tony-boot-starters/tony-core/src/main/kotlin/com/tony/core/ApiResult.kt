@file:Suppress("unused")

package com.tony.core

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.tony.core.utils.asTo
import com.tony.core.utils.defaultIfBlank

@JsonPropertyOrder(value = ["code", "msg", "data"])
data class ApiResult<T> @JvmOverloads constructor(
    val data: T?,
    val code: Int = ApiProperty.successCode,
    val msg: String = ""
) {

    companion object {
        @JvmField
        val EMPTY_RESULT = emptyMap<Any?, Any?>()

        @JvmSynthetic
        fun <T> T?.toOneResult() = OneResult(this)

        fun message(message: String?) = ApiResult(Unit, ApiProperty.successCode, message.defaultIfBlank())
    }
}

data class OneResult<T>(val result: T? = null)

data class ListResult<T> @JvmOverloads constructor(val items: Collection<T>? = listOf()) {

    constructor(array: Array<*>) : this(array.asList().asTo())
    constructor(byteArray: ByteArray) : this(byteArray.asList().asTo())
    constructor(shortArray: ShortArray) : this(shortArray.asList().asTo())
    constructor(intArray: IntArray) : this(intArray.asList().asTo())
    constructor(longArray: LongArray) : this(longArray.asList().asTo())
    constructor(floatArray: FloatArray) : this(floatArray.asList().asTo())
    constructor(doubleArray: DoubleArray) : this(doubleArray.asList().asTo())
    constructor(booleanArray: BooleanArray) : this(booleanArray.asList().asTo())
    constructor(charArray: CharArray) : this(charArray.asList().asTo())
}

@JsonPropertyOrder(value = ["page", "size", "total", "pages", "hasNext", "items"])
data class PageResult<T>(
    val items: Collection<T>?,
    val page: Long,
    val size: Long,
    val pages: Long,
    val total: Long,
    val hasNext: Boolean
) {

    constructor(
        array: Array<T>,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(array.asList(), page, size, pages, total, hasNext)

    constructor(
        byteArray: ByteArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(byteArray.asList().asTo(), page, size, pages, total, hasNext)

    constructor(
        shortArray: ShortArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(shortArray.asList().asTo(), page, size, pages, total, hasNext)

    constructor(
        intArray: IntArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(intArray.asList().asTo(), page, size, pages, total, hasNext)

    constructor(
        longArray: LongArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(longArray.asList().asTo(), page, size, pages, total, hasNext)

    constructor(
        floatArray: FloatArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(floatArray.asList().asTo(), page, size, pages, total, hasNext)

    constructor(
        doubleArray: DoubleArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(doubleArray.asList().asTo(), page, size, pages, total, hasNext)

    constructor(
        booleanArray: BooleanArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(booleanArray.asList().asTo(), page, size, pages, total, hasNext)

    constructor(
        charArray: CharArray,
        page: Long,
        size: Long,
        pages: Long,
        total: Long,
        hasNext: Boolean
    ) : this(charArray.asList().asTo(), page, size, pages, total, hasNext)

    inline fun <R> map(transform: (T) -> R) =
        PageResult(items?.map(transform), page, size, pages, total, hasNext)

    inline fun onEach(action: (T) -> Unit) =
        PageResult(items?.onEach(action), page, size, pages, total, hasNext)

    inline fun firstOrNull(predicate: (T) -> Boolean) = items?.firstOrNull(predicate)
}
