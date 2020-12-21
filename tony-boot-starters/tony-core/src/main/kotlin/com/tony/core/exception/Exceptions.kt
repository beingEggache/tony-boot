@file:Suppress("unused")

package com.tony.core.exception

import com.tony.core.BIZ_ERROR
import com.tony.core.INTERNAL_SERVER_ERROR

open class BaseException @JvmOverloads constructor(
    override val message: String? = "",
    open val code: Int,
    override val cause: Throwable? = null
) : RuntimeException(message, cause)

/**
 * 自定义框架层异常
 */
open class ApiException @JvmOverloads constructor(
    override val message: String? = "",
    override val code: Int = INTERNAL_SERVER_ERROR,
    throwable: Throwable? = null
) : BaseException(message, code, throwable) {

    constructor(message: String?, throwable: Throwable) : this(message, INTERNAL_SERVER_ERROR, throwable)
}

/**
 * 业务用异常
 */
open class BizException @JvmOverloads constructor(
    override val message: String,
    override val code: Int = BIZ_ERROR
) : BaseException(message, code)
