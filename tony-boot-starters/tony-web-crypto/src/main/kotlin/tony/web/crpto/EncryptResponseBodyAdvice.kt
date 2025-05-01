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

package tony.web.crpto

/**
 * 响应体加密
 * @author tangli
 * @date 2023/05/26 19:53
 */
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.core.MethodParameter
import org.springframework.core.PriorityOrdered
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
import tony.ApiResultLike
import tony.ENCRYPTED_HEADER_NAME
import tony.annotation.web.crypto.EncryptResponseBody
import tony.crypto.CryptoProvider
import tony.crypto.symmetric.encryptToString
import tony.utils.getLogger
import tony.utils.isTypesOrSubTypesOf
import tony.utils.toJsonString
import tony.utils.trimQuotes
import tony.web.WebContext

/**
 * 响应体加密
 * @author tangli
 * @date 2023/05/26 19:53
 */
public interface EncryptResponseBodyAdvice :
    PriorityOrdered,
    ResponseBodyAdvice<Any?> {
    public val cryptoProvider: CryptoProvider

    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Boolean =
        returnType.hasMethodAnnotation(EncryptResponseBody::class.java) &&
            converterType.isTypesOrSubTypesOf(
                StringHttpMessageConverter::class.java,
                MappingJackson2HttpMessageConverter::class.java
            )

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
    ): Any? {
        WebContext.response?.addHeader(ENCRYPTED_HEADER_NAME, "true")
        if (body != null && body is ApiResultLike<*>) {
            return if (body.success) {
                EncryptApiResult(
                    body.code,
                    body.message,
                    body
                        .data
                        .toJsonString()
                        .encryptToString(
                            cryptoProvider.algorithm,
                            cryptoProvider.secret,
                            cryptoProvider.encoding
                        )
                )
            } else {
                body
            }
        }
        return body
            .toJsonString()
            .trimQuotes()
            .encryptToString(
                cryptoProvider.algorithm,
                cryptoProvider.secret,
                cryptoProvider.encoding
            )
    }

    override fun getOrder(): Int =
        PriorityOrdered.LOWEST_PRECEDENCE
}

@ConditionalOnExpression("false")
@RestControllerAdvice
internal class DefaultEncryptResponseBodyAdvice(
    override val cryptoProvider: CryptoProvider,
) : EncryptResponseBodyAdvice {
    private val logger = getLogger()

    init {
        logger.info("Response body encrypt is enabled.")
    }
}
