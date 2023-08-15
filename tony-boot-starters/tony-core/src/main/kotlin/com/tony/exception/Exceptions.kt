package com.tony.exception

import com.tony.ApiProperty

/**
 * 全局异常类型基类.
 *
 * @param message 异常消息.
 * @param code 错误码.
 * @param cause 引用异常.
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
public open class BaseException
    @JvmOverloads
    constructor(
        override val message: String? = "",
        public open val code: Int,
        override val cause: Throwable? = null,
    ) : RuntimeException(message, cause)

/**
 * 自定义框架层异常.
 *
 * @param message 异常消息.
 * @param code 错误码. 默认 [ApiProperty.errorCode]
 * @param cause 引用异常.
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
public open class ApiException
    @JvmOverloads
    constructor(
        override val message: String? = "",
        override val code: Int = ApiProperty.errorCode,
        cause: Throwable? = null,
    ) : BaseException(message.toString(), code, cause) {

        public constructor(message: String?, cause: Throwable) : this(message, ApiProperty.errorCode, cause)
    }

/**
 * 全局业务用异常
 * @param message 异常消息.
 * @param code 错误码. 默认 [ApiProperty.preconditionFailedCode]
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
public open class BizException
    @JvmOverloads
    constructor(
        override val message: String,
        override val code: Int = ApiProperty.preconditionFailedCode,
    ) : BaseException(message, code)
