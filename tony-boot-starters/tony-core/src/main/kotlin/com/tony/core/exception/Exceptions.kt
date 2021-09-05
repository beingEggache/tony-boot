@file:Suppress("unused")

package com.tony.core.exception

import com.tony.core.ApiProperty

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
    override val code: Int = ApiProperty.errorCode,
    throwable: Throwable? = null
) : BaseException(message, code, throwable) {

    constructor(message: String?, throwable: Throwable) : this(message, ApiProperty.errorCode, throwable)
}

/**
 * 业务用异常
 */
open class BizException @JvmOverloads constructor(
    override val message: String,
    override val code: Int = ApiProperty.bizErrorCode
) : BaseException(message, code)
