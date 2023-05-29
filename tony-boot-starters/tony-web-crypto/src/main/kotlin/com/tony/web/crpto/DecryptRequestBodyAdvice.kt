package com.tony.web.crpto

import com.tony.crypto.symmetric.decryptToByte
import com.tony.web.crpto.config.WebCryptoProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.core.MethodParameter
import org.springframework.core.PriorityOrdered
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpInputMessage
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.lang.reflect.Type

/**
 * 将请求体解密, 目前只支持 json RequestBody
 * @author tangli
 * @since 2023/05/26 16:53
 */
@ConditionalOnExpression("\${web.crypto.enabled:false}")
@RestControllerAdvice
internal class DecryptRequestBodyAdvice(
    internal val webCryptoProperties: WebCryptoProperties,
) : PriorityOrdered, RequestBodyAdviceAdapter() {

    override fun supports(
        methodParameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Boolean {
        val method = methodParameter.method
        val controllerClass = method?.declaringClass

        val controllerHasAnnotation = controllerClass
            ?.annotations
            ?.map { it.annotationClass }
            ?.contains(ApiDecrypt::class) == true

        if (controllerHasAnnotation) {
            return true
        }

        return method
            ?.annotations
            ?.map { it.annotationClass }
            ?.contains(ApiDecrypt::class) == true &&
            methodParameter.hasParameterAnnotation(RequestBody::class.java)
    }

    override fun beforeBodyRead(
        inputMessage: HttpInputMessage,
        parameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>,
    ): HttpInputMessage {
        val bytes = inputMessage
            .body
            .readAllBytes()
            // application/json 类型给加密字符串多加了双引号.
            .run {
                copyOfRange(1, this.size - 1)
            }
        return DecryptHttpInputMessage(
            inputMessage.headers,
            ByteArrayInputStream(
                bytes
                    .decryptToByte(
                        webCryptoProperties.algorithm,
                        webCryptoProperties.secret,
                        webCryptoProperties.digestMode,
                    ),
            ),
        )
    }

    override fun getOrder(): Int = PriorityOrdered.HIGHEST_PRECEDENCE

    private class DecryptHttpInputMessage(
        private val httpHeaders: HttpHeaders,
        private val inputStream: InputStream,
    ) : HttpInputMessage {
        override fun getHeaders(): HttpHeaders {
            return httpHeaders
        }

        override fun getBody(): InputStream {
            return inputStream
        }
    }
}
