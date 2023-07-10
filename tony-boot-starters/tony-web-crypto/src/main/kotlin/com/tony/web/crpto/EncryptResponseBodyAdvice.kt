package com.tony.web.crpto

import com.tony.annotation.web.crypto.EncryptResponseBody
import com.tony.crypto.symmetric.encryptToString
import com.tony.utils.toJsonString
import com.tony.web.crpto.config.WebCryptoProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.core.MethodParameter
import org.springframework.core.PriorityOrdered
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

/**
 * 将请求体解密, 目前只支持 RequestBody
 * @author tangli
 * @since 2023/05/26 16:53
 */
@ConditionalOnExpression("\${web.crypto.enabled:false}")
@RestControllerAdvice
internal class EncryptResponseBodyAdvice(
    internal val webCryptoProperties: WebCryptoProperties,
) : PriorityOrdered, ResponseBodyAdvice<Any?> {

    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Boolean = returnType.hasMethodAnnotation(EncryptResponseBody::class.java)

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
    ): Any? {
        response.headers.contentType = MediaType.TEXT_PLAIN
        // TODO 这里会有个问题, 加密后的字符串会在前后加上双引号.
        return body
            .toJsonString()
            .encryptToString(
                webCryptoProperties.algorithm,
                webCryptoProperties.secret,
                webCryptoProperties.encoding
            )
    }

    override fun getOrder(): Int = PriorityOrdered.LOWEST_PRECEDENCE
}
