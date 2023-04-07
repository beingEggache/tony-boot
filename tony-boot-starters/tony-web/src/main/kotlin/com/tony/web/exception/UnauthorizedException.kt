package com.tony.web.exception

import com.tony.ApiProperty
import com.tony.exception.ApiException

public class UnauthorizedException @JvmOverloads constructor(
    override val message: String?,
    override val code: Int = ApiProperty.unauthorizedCode,
) : ApiException(message, code)
