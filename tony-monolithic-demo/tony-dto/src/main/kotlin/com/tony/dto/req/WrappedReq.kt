package com.tony.dto.req

import com.fasterxml.jackson.annotation.JsonUnwrapped
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

/**
 * WrappedReq is
 * @author tangli
 * @date 2023/07/10 19:25
 */
class WrappedReq<T> {
    @field:Valid
    @field:JsonUnwrapped
    @field:NotNull(message = "请输入")
    var value: T? = null
}
