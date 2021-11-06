package com.tony.openfeign.decoder

import com.tony.exception.ApiException
import feign.FeignException
import feign.Response
import feign.codec.ErrorDecoder
import java.lang.Exception

class DefaultErrorDecoder : ErrorDecoder {
    override fun decode(methodKey: String?, response: Response): Exception {
        val status = response.status()
        return if (status >= 400) {
            val url = response.request().url()
            ApiException(
                "$methodKey error,status:$status,reason:${response.reason()},url: $url"
            )
        } else FeignException.errorStatus(methodKey, response)
    }
}
