/**
 * tony-boot-starters
 * AlipayException
 *
 * @author Tang Li
 * @date 2021/10/22 15:31
 */
package com.tony.alipay.exception

import com.tony.ApiProperty
import com.tony.exception.ApiException

/**
 * 支付宝 异常
 * @author Tang Li
 * @date 2023/09/12 17:59
 * @since 1.0.0
 */
public class AlipayException
    @JvmOverloads
    constructor(
        override val message: String? = "",
        override val cause: Throwable? = null,
        override val code: Int = ApiProperty.errorCode,
    ) : ApiException(message, code, cause)
