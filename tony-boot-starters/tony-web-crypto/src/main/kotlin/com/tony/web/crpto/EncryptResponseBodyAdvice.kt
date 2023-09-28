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
 * 响应体加密
 * @author Tang Li
 * @date 2023/05/26 16:53
 */
import com.tony.ApiResultLike
import com.tony.EncryptApiResult
import com.tony.annotation.web.crypto.EncryptResponseBody
import com.tony.codec.enums.Encoding
import com.tony.crypto.symmetric.encryptToString
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
 * 响应体加密
 * @author Tang Li
 * @date 2023/05/26 16:53
 */
public interface EncryptResponseBodyAdvice : PriorityOrdered, ResponseBodyAdvice<Any?> {

    public val algorithm: SymmetricCryptoAlgorithm

    public val secret: String

    public val encoding: Encoding
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
    override val encoding: Encoding,
) : EncryptResponseBodyAdvice
