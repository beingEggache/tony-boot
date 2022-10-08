package com.tony.exception

import com.tony.ApiProperty

public open class BaseException @JvmOverloads constructor(
    override val message: String? = "",
    public open val code: Int,
    override val cause: Throwable? = null
) : RuntimeException(message, cause)

/**
 * 自定义框架层异常
 */
public open class ApiException @JvmOverloads constructor(
    override val message: String? = "",
    override val code: Int = ApiProperty.errorCode,
    throwable: Throwable? = null
) : BaseException(message, code, throwable) {

    public constructor(message: String?, throwable: Throwable) : this(message, ApiProperty.errorCode, throwable)
}

/**
 * 业务用异常
 */
public open class BizException @JvmOverloads constructor(
    override val message: String,
    override val code: Int = ApiProperty.bizErrorCode
) : BaseException(message, code)
