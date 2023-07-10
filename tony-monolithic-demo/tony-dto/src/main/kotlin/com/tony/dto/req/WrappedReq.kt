package com.tony.dto.req

import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.tony.annotation.web.support.InjectEmptyIfNull
import javax.validation.Valid

/**
 * WrappedReq is
 * @author tangli
 * @since 2023/07/10 14:25
 */
class WrappedReq<T> {
    @field:Valid
    @field:JsonUnwrapped
    @field:InjectEmptyIfNull
    var value: T? = null
}
