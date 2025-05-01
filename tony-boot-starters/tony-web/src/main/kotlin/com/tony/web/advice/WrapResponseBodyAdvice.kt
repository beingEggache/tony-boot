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

package com.tony.web.advice

import com.tony.ApiProperty
import com.tony.ApiResult
import com.tony.ListResult
import com.tony.misc.notSupportResponseWrapClassCollection
import com.tony.utils.antPathMatchAny
import com.tony.utils.asTo
import com.tony.utils.getLogger
import com.tony.utils.isArrayLikeType
import com.tony.utils.isTypesOrSubTypesOf
import com.tony.web.WebContext
import com.tony.web.utils.url
import java.util.Collections
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

/**
 * 响应包装, 将直接返回的类型包装为全局结构 [ApiResult].
 *
 * @author tangli
 * @date 2023/05/25 19:51
 */
@ConditionalOnExpression("false")
@RestControllerAdvice
internal class WrapResponseBodyAdvice : ResponseBodyAdvice<Any?> {
    private val logger = getLogger()

    init {
        logger.info("Response wrap is enabled")
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
    ): ApiResult<*> =
        when {
            body == null -> {
                ApiResult(Unit, ApiProperty.okCode)
            }

            !body::class.java
                .isArrayLikeType() -> {
                ApiResult(body, ApiProperty.okCode)
            }

            else -> {
                if (body::class.java
                        .isArray
                ) {
                    ApiResult(toListResult(body), ApiProperty.okCode)
                } else {
                    ApiResult(ListResult(body.asTo()), ApiProperty.okCode)
                }
            }
        }

    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>,
    ) =
        !WebContext
            .request
            .url
            .path
            .antPathMatchAny(WebContext.responseWrapExcludePatterns) &&
            converterType.isTypesOrSubTypesOf(MappingJackson2HttpMessageConverter::class.java) &&
            !returnType
                .parameterType
                .isTypesOrSubTypesOf(notSupportResponseWrapClassCollection)

    private companion object Utils {
        @JvmStatic
        private fun toListResult(body: Any?) =
            when (body) {
                is ByteArray -> ListResult<Byte>(body)
                is ShortArray -> ListResult<Short>(body)
                is IntArray -> ListResult<Int>(body)
                is LongArray -> ListResult<Long>(body)
                is FloatArray -> ListResult<Float>(body)
                is DoubleArray -> ListResult<Double>(body)
                is BooleanArray -> ListResult<Boolean>(body)
                is CharArray -> ListResult<Char>(body)
                is Array<*> -> ListResult<Any>(body)
                else -> ListResult(Collections.EMPTY_LIST)
            }
    }
}
