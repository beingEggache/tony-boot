package com.tony.flow.exception

import com.tony.ApiProperty
import com.tony.exception.BaseException

/**
 * FlowException is
 * @author tangli
 * @date 2023/10/20 16:40
 * @since 1.0.0
 */
public class FlowException
@JvmOverloads
constructor(
    override val message: String? = "",
    override val code: Int = ApiProperty.errorCode,
    cause: Throwable? = null,
) : BaseException(message.toString(), code, cause)
