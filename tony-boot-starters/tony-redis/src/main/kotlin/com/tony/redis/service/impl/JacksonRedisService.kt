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

package com.tony.redis.service.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.enums.EnumValue
import com.tony.enums.IntEnumCreator
import com.tony.enums.IntEnumValue
import com.tony.enums.StringEnumCreator
import com.tony.enums.StringEnumValue
import com.tony.redis.serializer.SerializerMode
import com.tony.redis.service.RedisService
import com.tony.utils.asTo
import com.tony.utils.isNumberTypes
import com.tony.utils.isStringLikeType
import com.tony.utils.isTypesOrSubTypesOf
import com.tony.utils.jsonToObj
import com.tony.utils.rawClass
import com.tony.utils.toJsonString
import com.tony.utils.toNumber
import com.tony.utils.trimQuotes

internal class JacksonRedisService : RedisService {
    override val serializerMode: SerializerMode = SerializerMode.JACKSON

    override fun Any.inputTransformTo(): Any =
        if (this::class.java
                .isNumberTypes()
        ) {
            this
        } else {
            this
                .toJsonString()
                .trimQuotes()
        }

    override fun <T : Any> Any?.outputTransformTo(type: Class<T>): T? =
        jsonToObjWithTypeClass(type) {
            it
                .trimQuotes()
                .jsonToObj(type)
        }

    override fun <T : Any> Any?.outputTransformTo(type: JavaType): T? =
        jsonToObjWithTypeClass(type.rawClass()) {
            it
                .trimQuotes()
                .jsonToObj(type)
        }

    override fun <T : Any> Any?.outputTransformTo(type: TypeReference<T>): T? =
        jsonToObjWithTypeClass(type.rawClass()) {
            it
                .trimQuotes()
                .jsonToObj(type)
        }

    private fun <T : Any> Any?.jsonToObjWithTypeClass(
        type: Class<T>,
        func: (String) -> T,
    ): T? {
        if (this == null) {
            return null
        }
        return when {
            type.isNumberTypes() -> toNumber(type)
            type.isStringLikeType() -> toString().trimQuotes()
            type == EnumValue::class.java && this is EnumValue<*> -> this
            type.isTypesOrSubTypesOf(StringEnumValue::class.java) -> {
                StringEnumCreator
                    .getCreator(type)
                    .create(toString().trimQuotes())
            }

            type.isTypesOrSubTypesOf(IntEnumValue::class.java) -> {
                IntEnumCreator
                    .getCreator(type)
                    .create(toString().toInt())
            }

            this.isTypesOrSubTypesOf(type) -> this
            else -> func(toString())
        }.asTo()
    }
}
