package com.tony.web.crpto

import com.tony.ApiResultLike
import com.tony.EncryptApiResult
import com.tony.annotation.web.crypto.EncryptResponseBody
import com.tony.crypto.symmetric.encryptToString
import com.tony.crypto.symmetric.enums.CryptoEncoding
import com.tony.crypto.symmetric.enums.SymmetricCryptoAlgorithm
import com.tony.utils.toJsonString
import com.tony.utils.trimQuotes
import com.tony.web.WebContext
import com.tony.web.utils.isTextMediaTypes
import com.tony.web.utils.parsedMedia
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
public interface EncryptResponseBodyAdvice : PriorityOrdered, ResponseBodyAdvice<Any?> {

    public val algorithm: SymmetricCryptoAlgorithm

    public val secret: String

    public val encoding: CryptoEncoding
    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Boolean = returnType.hasMethodAnnotation(EncryptResponseBody::class.java) &&
        isTextMediaTypes(WebContext.request.parsedMedia)

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
    ): Any? {
        if (body != null && body is ApiResultLike<*>) {
            return if (body.success) {
                EncryptApiResult().apply {
                    code = body.code
                    message = body.message
                    data = body.data.toJsonString()
                        .encryptToString(
                            algorithm,
                            secret,
                            encoding
                        )
                }
            } else {
                body
            }
        }
        return body
            .toJsonString()
            .trimQuotes()
            .encryptToString(
                algorithm,
                secret,
                encoding
            )
    }
    override fun getOrder(): Int = PriorityOrdered.LOWEST_PRECEDENCE
}

@RestControllerAdvice
internal class DefaultEncryptResponseBodyAdvice(
    override val algorithm: SymmetricCryptoAlgorithm,
    override val secret: String,
    override val encoding: CryptoEncoding,
) : EncryptResponseBodyAdvice {

    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Boolean = returnType.hasMethodAnnotation(EncryptResponseBody::class.java) &&
        isTextMediaTypes(WebContext.request.parsedMedia)

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
    ): Any? {
        if (body != null &&
            body is ApiResultLike<*>
        ) {
            return if (body.success) {
                EncryptApiResult().apply {
                    code = body.code
                    message = body.message
                    data = body.data.toJsonString()
                        .encryptToString(
                            algorithm,
                            secret,
                            encoding
                        )
                }
            } else {
                body
            }
        }
        return body
            .toJsonString()
            .trimQuotes()
            .encryptToString(
                algorithm,
                secret,
                encoding
            )
    }

    override fun getOrder(): Int = PriorityOrdered.LOWEST_PRECEDENCE
}
