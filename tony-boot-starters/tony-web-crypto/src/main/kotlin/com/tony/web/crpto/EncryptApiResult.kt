package com.tony.web.crpto

import com.tony.ApiResultLike

/**
 * 加密请求响应结构
 *
 * @author Tang Li
 * @date 2023/08/08 11:30
 */
internal data class EncryptApiResult(
    private val code: Int,
    private val message: CharSequence,
    private val data: String,
) : ApiResultLike<String> {
    override fun getData(): String =
        data

    override fun getCode(): Int =
        code

    override fun getMessage(): CharSequence =
        message
}
