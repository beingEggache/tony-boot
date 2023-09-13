package com.tony.feign.codec

import com.tony.exception.ApiException
import feign.Response
import feign.codec.ErrorDecoder

/**
 * Feign 错误默认解析器.
 * 抛出全局统一框架层异常 [ApiException]
 * @author Tang Li
 * @date 2023/09/13 10:33
 * @since 1.0.0
 */
public class DefaultErrorDecoder : ErrorDecoder {

    private val default = ErrorDecoder.Default()
    override fun decode(methodKey: String?, response: Response): Exception {
        val status = response.status()
        return if (status in 400..<500) {
            val url = response.request().url()
            ApiException(
                "$methodKey error,status:$status,reason:${response.reason()},url: $url",
                status * 100
            )
        } else {
            default.decode(methodKey, response)
        }
    }
}
