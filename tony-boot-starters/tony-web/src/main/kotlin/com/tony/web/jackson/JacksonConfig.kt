@file:JvmName("JacksonConfig")

package com.tony.web.jackson

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

internal class NullStrJsonSerializer : JsonSerializer<Any?>() {
    override fun serialize(
        value: Any?,
        gen: JsonGenerator,
        serializers: SerializerProvider,
    ) {
        gen.writeString("")
    }
}

public class NullValueBeanSerializerModifier : BeanSerializerModifier() {

    private val nullArrayJsonSerializer = NullArrayJsonSerializer()
    private val nullObjJsonSerializer = NullObjJsonSerializer()
    private val nullStrJsonSerializer = NullStrJsonSerializer()

    override fun changeProperties(
        config: SerializationConfig,
        beanDesc: BeanDescription,
        beanProperties: MutableList<BeanPropertyWriter>,
    ): MutableList<BeanPropertyWriter> = beanProperties.onEach {
        when {
            it.type.isDateTimeLikeType() || it.type.isBooleanType() || it.type.isEnumType -> Unit
            it.type.isArrayLikeType() -> it.assignNullSerializer(nullArrayJsonSerializer)
            it.type.isObjLikeType() -> it.assignNullSerializer(nullObjJsonSerializer)
            it.type.isStringLikeType() -> it.assignNullSerializer(nullStrJsonSerializer)
        }
    }
}
