/**
 * tony-boot-starters
 * AlipayException
 *
 * TODO
 *
 * @author tangli
 * @since 2021/10/22 15:31
 */
package com.tony.alipay.exception

import com.tony.ApiProperty
import com.tony.exception.ApiException

class AlipayException @JvmOverloads constructor(
    override val message: String? = "",
    override val cause: Throwable? = null,
    override val code: Int = ApiProperty.errorCode
) : ApiException(message, code, cause)
