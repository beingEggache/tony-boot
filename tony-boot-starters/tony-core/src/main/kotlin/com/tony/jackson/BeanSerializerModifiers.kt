@file:JvmName("BeanSerializerModifiers")

package com.tony.jackson

/**
 * jackson 相关类
 *
 *
 * @author Tang Li
 * @date 2022/4/24 16:44
 */
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import com.tony.ApiResult
import com.tony.utils.isArrayLikeType
import com.tony.utils.isBooleanType
import com.tony.utils.isDateTimeLikeType
import com.tony.utils.isObjLikeType
import com.tony.utils.isStringLikeType

/**
 * 数组或列表类型为 null 时输出 "[]"
 *
 * @author Tang Li
 * @date 2023/5/25 10:37
 */
internal class NullArrayJsonSerializer : JsonSerializer<Any?>() {

    override fun serialize(
        value: Any?,
        gen: JsonGenerator,
        serializers: SerializerProvider,
    ) {
        if (value == null) {
            gen.writeArray(emptyArray(), 0, 0)
        }
    }
}

/**
 * 对象或 map 类型 为null 时, 输出 "{}"
 *
 * @author Tang Li
 * @date 2023/5/25 10:39
 */
internal class NullObjJsonSerializer : JsonSerializer<Any?>() {
    override fun serialize(
        value: Any?,
        gen: JsonGenerator,
        serializers: SerializerProvider?,
    ) {
        if (value == null) {
            gen.writeObject(ApiResult.EMPTY_RESULT)
        }
    }
}

/**
 * 字符串为null时输出空字符串
 *
 * @author Tang Li
 * @date 2023/5/25 10:40
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
 * @author Tang Li
 * @date 2023/5/25 10:40
 */
public class NullValueBeanSerializerModifier : BeanSerializerModifier() {

    private val nullArrayJsonSerializer = NullArrayJsonSerializer()
    private val nullObjJsonSerializer = NullObjJsonSerializer()
    private val nullStrJsonSerializer = NullStrJsonSerializer()

    override fun changeProperties(
        config: SerializationConfig,
        beanDesc: BeanDescription,
        beanProperties: MutableList<BeanPropertyWriter>,
    ): MutableList<BeanPropertyWriter> = beanProperties.onEach {
        val type = it.type
        when {
            type.isStringLikeType() || type.isDateTimeLikeType() -> it.assignNullSerializer(nullStrJsonSerializer)
            type.isArrayLikeType() -> it.assignNullSerializer(nullArrayJsonSerializer)
            type.isBooleanType() || type.isEnumType -> Unit
            type.isObjLikeType() -> it.assignNullSerializer(nullObjJsonSerializer)
        }
    }
}
