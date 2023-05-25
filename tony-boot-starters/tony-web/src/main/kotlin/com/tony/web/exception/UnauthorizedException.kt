package com.tony.web.exception

import com.tony.ApiProperty
import com.tony.exception.ApiException

/**
 * 用户未认证异常.
 *
 * @property message
 * @property code 默认为 [ApiProperty.unauthorizedCode]
 */
public class UnauthorizedException @JvmOverloads constructor(
    override val message: String?,
    override val code: Int = ApiProperty.unauthorizedCode,
) : ApiException(message, code)
