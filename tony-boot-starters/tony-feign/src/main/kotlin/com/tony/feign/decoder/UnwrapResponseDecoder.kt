package com.tony.feign.decoder

import com.tony.ApiResultLike
import com.tony.utils.isTypesOrSubTypesOf
import com.tony.utils.rawClass
import com.tony.wrapResponseHeaderName
import feign.Response
import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer
import org.springframework.cloud.openfeign.support.SpringDecoder
import java.lang.reflect.Type

/**
 * ReponseUnwrapDecoder is
 * @author tangli
 * @since 2023/06/19 18:11
 */
public class UnwrapResponseDecoder(
    messageConverters: ObjectFactory<HttpMessageConverters>,
    customizers: ObjectProvider<HttpMessageConverterCustomizer>,
) : SpringDecoder(messageConverters, customizers) {

    override fun decode(response: Response?, type: Type?): Any {
        if(response?.headers()?.containsKey(wrapResponseHeaderName) != true){
            return super.decode(response, type)
        }
        if(type?.rawClass()?.isTypesOrSubTypesOf(ApiResultLike::class.java) == true){
            return super.decode(response, type)
        }

    }
}
