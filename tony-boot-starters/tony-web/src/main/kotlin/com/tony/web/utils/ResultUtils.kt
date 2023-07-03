@file:JvmName("ResultUtils")

package com.tony.web.utils

import com.tony.ApiResult
import com.tony.OneResult

/**
 * ResultUtils
 *
 * @author tangli
 * @since 2023/7/03 20:57
 */

public fun Boolean.ofResult(): ApiResult<OneResult<Boolean>> = ApiResult.of(this)

public fun <E : CharSequence> E.ofResult(): ApiResult<OneResult<E>> = ApiResult.of(this)

public fun <E : Number> E.ofResult(): ApiResult<OneResult<E>> = ApiResult.of(this)

public fun <E : Enum<*>> E.ofResult(): ApiResult<OneResult<E>> = ApiResult.of(this)
