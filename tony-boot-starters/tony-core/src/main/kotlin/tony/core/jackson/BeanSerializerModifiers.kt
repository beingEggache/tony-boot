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

@file:JvmName("BeanSerializerModifiers")

package tony.core.jackson

/**
 * jackson 相关类
 *
 *
 * @author tangli
 * @date 2022/04/24 19:44
 */
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import tony.core.utils.isArrayLikeType
import tony.core.utils.isBooleanType
import tony.core.utils.isDateTimeLikeType
import tony.core.utils.isObjLikeType
import tony.core.utils.isStringLikeType

/**
 * 数组或列表类型为 null 时输出 "[]"
 *
 * @author tangli
 * @date 2023/05/25 19:37
 */
internal class NullArrayJsonSerializer : JsonSerializer<Any?>() {
    override fun serialize(
        value: Any?,
        gen: JsonGenerator,
        serializers: SerializerProvider,
    ) {
        if (value == null) {
            gen.writeStartArray()
            gen.writeEndArray()
        }
    }
}

/**
 * 对象或 map 类型 为null 时, 输出 "{}"
 *
 * @author tangli
 * @date 2023/05/25 19:39
 */
internal class NullObjJsonSerializer : JsonSerializer<Any?>() {
    override fun serialize(
        value: Any?,
        gen: JsonGenerator,
        serializers: SerializerProvider?,
    ) {
        if (value == null) {
            gen.writeStartObject()
            gen.writeEndObject()
        }
    }
}

/**
 * 字符串为null时输出空字符串
 *
 * @author tangli
 * @date 2023/05/25 19:40
 */
internal class NullStrJsonSerializer : JsonSerializer<Any?>() {
    override fun serialize(
        value: Any?,
        gen: JsonGenerator,
        serializers: SerializerProvider,
    ) {
        gen.writeString("")
    }
}

/**
 * 当json 输出对象字段为null时改变行为.
 *
 * 比如字符串为null时输出空字符串.
 *
 * 对象或 map 类型 为null 时, 输出 "{}".
 *
 * 数组或列表类型为 null 时输出 "[]".
 *
 * @author tangli
 * @date 2023/05/25 19:40
 */
public class NullValueBeanSerializerModifier : BeanSerializerModifier() {
    private val nullArrayJsonSerializer = NullArrayJsonSerializer()
    private val nullObjJsonSerializer = NullObjJsonSerializer()
    private val nullStrJsonSerializer = NullStrJsonSerializer()

    override fun changeProperties(
        config: SerializationConfig,
        beanDesc: BeanDescription,
        beanProperties: MutableList<BeanPropertyWriter>,
    ): MutableList<BeanPropertyWriter> =
        beanProperties.onEach {
            val type = it.type
            when {
                type.isStringLikeType() -> {
                    it.assignNullSerializer(nullStrJsonSerializer)
                }

                type.isDateTimeLikeType() &&
                    it.member.getAnnotation(JsonSetter::class.java)?.nulls == Nulls.AS_EMPTY -> {
                    it.assignNullSerializer(nullStrJsonSerializer)
                }

                type.isArrayLikeType() -> {
                    it.assignNullSerializer(nullArrayJsonSerializer)
                }

                type.isBooleanType() || type.isEnumType -> {
                }

                type.isObjLikeType() -> {
                    it.assignNullSerializer(nullObjJsonSerializer)
                }
            }
        }
}
