/**
 * tony-dependencies
 * SignInvalidException
 *
 * TODO
 *
 * @author tangli
 * @since 2021/12/20 11:22
 */
package com.tony.feign.exception

import com.tony.ApiProperty
import com.tony.exception.BizException

class SignInvalidException @JvmOverloads constructor(
    override val message: String,
    override val code: Int = ApiProperty.validationErrorCode
) : BizException(message, code)
