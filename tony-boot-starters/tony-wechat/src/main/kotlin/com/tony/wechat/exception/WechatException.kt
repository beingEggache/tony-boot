/**
 * tony-boot-starters
 * WechatException
 *
 * @author tangli
 * @since 2021/10/22 15:28
 */
package com.tony.wechat.exception

import com.tony.ApiProperty
import com.tony.exception.ApiException

public class WechatException @JvmOverloads constructor(
    override val message: String? = "",
    override val code: Int = ApiProperty.errorCode,
) : ApiException(message, code)
