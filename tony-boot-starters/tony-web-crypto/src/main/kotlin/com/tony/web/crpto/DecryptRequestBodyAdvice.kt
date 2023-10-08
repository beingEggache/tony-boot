/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony.web.crpto
/**
 * 请求体解密, 目前只支持 RequestBody
 * @author Tang Li
 * @date 2023/05/26 16:53
 */
import com.tony.annotation.web.crypto.DecryptRequestBody
import com.tony.codec.enums.Encoding
import com.tony.crypto.symmetric.decryptToBytes
import com.tony.crypto.symmetric.enums.SymmetricCryptoAlgorithm
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.lang.reflect.Type
import org.springframework.core.MethodParameter
import org.springframework.core.PriorityOrdered
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpInputMessage
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice

/**
 * 将请求体解密, 目前只支持 RequestBody
 * @author Tang Li
 * @date 2023/05/26 16:53
 */
public interface DecryptRequestBodyAdvice : PriorityOrdered, RequestBodyAdvice {
    public val algorithm: SymmetricCryptoAlgorithm
    public val secret: String
    public val encoding: Encoding

    override fun supports(
        methodParameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Boolean = methodParameter.hasMethodAnnotation(DecryptRequestBody::class.java) &&
        methodParameter.hasParameterAnnotation(RequestBody::class.java)

    override fun beforeBodyRead(
        inputMessage: HttpInputMessage,
        parameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>,
    ): HttpInputMessage {
        val bytes =
            inputMessage
                .body
                .readAllBytes()
                .run {
                    // application/json 类型给加密字符串多加了双引号.
                    if (inputMessage.headers.contentType?.includes(MediaType.APPLICATION_JSON) == true) {
                        copyOfRange(1, this.size - 1)
                    } else {
                        this
                    }
                }
        return DecryptHttpInputMessage(
            inputMessage.headers.apply { contentType = MediaType.TEXT_PLAIN },
            ByteArrayInputStream(
                bytes
                    .decryptToBytes(
                        algorithm,
                        secret,
                        encoding
                    )
            )
        )
    }

    override fun getOrder(): Int = PriorityOrdered.HIGHEST_PRECEDENCE

    override fun afterBodyRead(
        body: Any,
        inputMessage: HttpInputMessage,
        parameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Any = body

    override fun handleEmptyBody(
        body: Any?,
        inputMessage: HttpInputMessage,
        parameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Any? = body

    private class DecryptHttpInputMessage(
        private val httpHeaders: HttpHeaders,
        private val inputStream: InputStream,
    ) : HttpInputMessage {
        override fun getHeaders(): HttpHeaders = httpHeaders

        override fun getBody(): InputStream = inputStream
    }
}

@RestControllerAdvice
internal class DefaultDecryptRequestBodyAdvice(
    override val algorithm: SymmetricCryptoAlgorithm,
    override val secret: String,
    override val encoding: Encoding,
) : DecryptRequestBodyAdvice
