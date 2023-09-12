/**
 * SignInvalidException
 *
 * TODO
 *
 * @author Tang Li
 * @date 2021/12/20 11:22
 */
package com.tony.feign.test.exception

import com.tony.ApiProperty
import com.tony.exception.BizException

class SignInvalidException @JvmOverloads constructor(
    override val message: String,
    override val code: Int = ApiProperty.badRequestCode
) : BizException(message, code)
