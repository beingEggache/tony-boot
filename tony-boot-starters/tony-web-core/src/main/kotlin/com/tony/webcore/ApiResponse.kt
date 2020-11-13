@file:Suppress("unused")
@file:JvmName("ResponseUtils")

package com.tony.webcore

import com.tony.core.ApiResult
import com.tony.core.BAD_REQUEST
import com.tony.core.INTERNAL_SERVER_ERROR
import com.tony.core.OneResult

fun <T> T?.toOneResult() =
    OneResult(this)

@JvmOverloads
fun errorResponse(msg: String = "", code: Int = INTERNAL_SERVER_ERROR) =
    ApiResult(OneResult(null), code, msg)

@JvmOverloads
fun badRequest(msg: String = "", code: Int = BAD_REQUEST) =
    ApiResult(OneResult(null), code, msg)

