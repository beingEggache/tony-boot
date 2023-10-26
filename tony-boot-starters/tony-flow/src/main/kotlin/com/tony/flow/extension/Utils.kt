package com.tony.flow.extension

import com.tony.ApiProperty
import com.tony.flow.exception.FlowException
import com.tony.utils.throwIf

/**
 * Utils is
 * @author tangli
 * @date 2023/10/26 15:50
 * @since 1.0.0
 */
internal fun flowThrowIf(
    condition: Boolean,
    message: String,
    code: Int = ApiProperty.preconditionFailedCode,
) {
    throwIf(condition, message, code, ::FlowException)
}

@JvmOverloads
public fun <T> T?.flowThrowIfNull(
    message: String = ApiProperty.notFoundMessage,
    code: Int = ApiProperty.notFoundCode,
): T {
    throwIf(this == null, message, code, ::FlowException)
    return this!!
}
