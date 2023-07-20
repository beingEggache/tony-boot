package com.tony.dto.req

import com.fasterxml.jackson.annotation.JsonUnwrapped
import javax.validation.Valid
import javax.validation.constraints.NotNull

/**
 * WrappedReq is
 * @author tangli
 * @since 2023/07/10 14:25
 */
class WrappedReq<T> {
    @field:Valid
    @field:JsonUnwrapped
    @field:NotNull(message = "请输入")
    var value: T? = null
}
