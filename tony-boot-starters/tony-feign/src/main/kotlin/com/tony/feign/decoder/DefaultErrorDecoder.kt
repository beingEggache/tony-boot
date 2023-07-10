package com.tony.feign.decoder

import com.tony.exception.ApiException
import feign.FeignException
import feign.Response
import feign.codec.ErrorDecoder
import java.lang.Exception

/**
 * Feign 错误默认解析器.
 * 抛出全局统一框架层异常 [ApiException]
 * @author tangli
 * @since 2023/5/25 15:44
 */
public class DefaultErrorDecoder : ErrorDecoder {
    override fun decode(methodKey: String?, response: Response): Exception {
        val status = response.status()
        return if (status >= 400) {
            val url = response.request().url()
            ApiException(
                "$methodKey error,status:$status,reason:${response.reason()},url: $url"
            )
        } else {
            FeignException.errorStatus(methodKey, response)
        }
    }
}
