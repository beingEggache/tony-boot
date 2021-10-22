/**
 * tony-boot-starters
 * WechatException
 *
 * TODO
 *
 * @author tangli
 * @since 2021/10/22 15:28
 */
package com.tony.wechat.exception

import com.tony.core.ApiProperty
import com.tony.core.exception.ApiException

class WechatException @JvmOverloads constructor(
    override val message: String? = "",
    override val code: Int = ApiProperty.errorCode
) : ApiException(message, code)
