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

package tony.redis.service.impl

import tony.enums.EnumValue
import tony.enums.IntEnumCreator
import tony.enums.IntEnumValue
import tony.enums.StringEnumCreator
import tony.enums.StringEnumValue
import tony.redis.serializer.SerializerMode
import tony.redis.service.RedisService
import tony.redis.toNum
import tony.utils.asTo
import tony.utils.isNumberTypes
import tony.utils.isStringLikeType
import tony.utils.isTypesOrSubTypesOf
import tony.utils.jsonToObj
import tony.utils.toJsonString
import tony.utils.trimQuotes

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
        jsonToObjWithTypeClass(type)

    private fun <T : Any> Any?.jsonToObjWithTypeClass(type: Class<T>): T? =
        when {
            this == null -> {
                null
            }

            type.isNumberTypes() -> {
                toNum(type)
            }

            type.isStringLikeType() -> {
                toString().trimQuotes()
            }

            type == EnumValue::class.java && this is EnumValue<*> -> {
                this
            }

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

            this.isTypesOrSubTypesOf(type) -> {
                this
            }

            else -> {
                toString()
                    .trimQuotes()
                    .jsonToObj(type)
            }
        }.asTo()
}
