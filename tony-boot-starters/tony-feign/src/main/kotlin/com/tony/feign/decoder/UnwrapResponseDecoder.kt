package com.tony.feign.decoder

import com.tony.ApiResultLike
import com.tony.exception.ApiException
import com.tony.utils.isTypesOrSubTypesOf
import com.tony.utils.jsonNode
import com.tony.utils.jsonToObj
import com.tony.utils.rawClass
import com.tony.utils.toJavaType
import com.tony.wrapExceptionHeaderName
import com.tony.wrapResponseHeaderName
import feign.Response
import java.lang.reflect.Type
import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer
import org.springframework.cloud.openfeign.support.SpringDecoder

/**
 * ReponseUnwrapDecoder is
 * @author tangli
 * @since 2023/06/19 18:11
 */
public class UnwrapResponseDecoder(
    messageConverters: ObjectFactory<HttpMessageConverters>,
    customizers: ObjectProvider<HttpMessageConverterCustomizer>,
) : SpringDecoder(messageConverters, customizers) {

    override fun decode(response: Response, type: Type): Any {
        if (response.headers().containsKey(wrapExceptionHeaderName)) {
            val jsonNode = response.body().asInputStream().jsonNode()
            val message = jsonNode.get(ApiResultLike<*>::message.name).asText()
            val code = jsonNode.get(ApiResultLike<*>::code.name).asInt()
            throw ApiException(message, code)
        }
        if (response.headers().containsKey(wrapResponseHeaderName)) {
            if (type.rawClass().isTypesOrSubTypesOf(ApiResultLike::class.java)) {
                return super.decode(response, type)
            }
            val jsonNode = response.body().asInputStream().jsonNode()
            return jsonNode
                .get(ApiResultLike<*>::data.name)
                .toString()
                .jsonToObj(type.toJavaType())
        }
        return super.decode(response, type)
    }
}
