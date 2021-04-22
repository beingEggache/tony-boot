package com.tony.webcore.jackson

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import com.tony.core.exception.ApiException
import com.tony.core.utils.createObjectMapper
import com.tony.core.utils.getLogger
import com.tony.core.utils.isArrayLikeType
import com.tony.core.utils.isBooleanType
import com.tony.core.utils.isDateTimeLikeType
import com.tony.core.utils.isObjLikeType
import com.tony.core.utils.isStringLikeType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

@Configuration
internal class JacksonConfig {

    @Bean
    fun mappingJackson2HttpMessageConverter() = MappingJackson2HttpMessageConverter().apply {
        objectMapper = createObjectMapper().apply {
            serializerFactory =
                serializerFactory
                    .withSerializerModifier(NullValueBeanSerializerModifier())
        }
    }
}

internal class NullArrayJsonSerializer : JsonSerializer<Any?>() {

    override fun serialize(
        value: Any?,
        gen: JsonGenerator,
        serializers: SerializerProvider
    ) {
        if (value == null) {
            gen.writeRaw("[]")
        }
    }
}

internal class NullObjJsonSerializer : JsonSerializer<Any?>() {
    override fun serialize(
        value: Any?,
        gen: JsonGenerator,
        serializers: SerializerProvider?
    ) {
        if (value == null) {
            gen.writeRaw("{}")
        }
    }
}

internal class NullStrJsonSerializer : JsonSerializer<Any?>() {
    override fun serialize(
        value: Any?,
        gen: JsonGenerator,
        serializers: SerializerProvider
    ) {
        gen.writeString("")
    }
}

internal class NullValueBeanSerializerModifier : BeanSerializerModifier() {

    private val nullArrayJsonSerializer = NullArrayJsonSerializer()
    private val nullObjJsonSerializer = NullObjJsonSerializer()
    private val nullStrJsonSerializer = NullStrJsonSerializer()

    override fun changeProperties(
        config: SerializationConfig,
        beanDesc: BeanDescription,
        beanProperties: MutableList<BeanPropertyWriter>
    ) = beanProperties.onEach {
        when {
            it.type.isDateTimeLikeType() || it.type.isBooleanType() || it.type.isEnumType -> Unit
            it.type.isArrayLikeType() -> it.assignNullSerializer(nullArrayJsonSerializer)
            it.type.isObjLikeType() -> it.assignNullSerializer(nullObjJsonSerializer)
            it.type.isStringLikeType() -> it.assignNullSerializer(nullStrJsonSerializer)
        }
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = MaskSerializer::class)
annotation class MaskConverter(
    val value: String = NAME
) {
    companion object {
        fun getMaskFun(maskType: String) =
            maskConverters[maskType.toLowerCase()] ?: throw ApiException("$maskType converter not found")
    }
}

const val MOBILE = "mobile"
const val NAME = "name"

private val maskConverters: MutableMap<String, (String) -> String> = mutableMapOf(
    MOBILE to { input: String ->
        "${input.substring(0, 2)}****${input.substring(input.length - 4, input.length)}"
    },
    NAME to { input: String ->
        input.replaceRange(1 until input.length, "**")
    }
)

class MaskSerializer : JsonSerializer<Any>() {

    override fun serialize(value: Any, gen: JsonGenerator, serializers: SerializerProvider) {
        val annotation = gen.currentValue
            .javaClass
            .getDeclaredField(gen.outputContext.currentName)
            .getAnnotation(MaskConverter::class.java)

        val maskFun = annotation.value.toLowerCase()
        val function = maskConverters[maskFun.toLowerCase()] ?: throw ApiException("$maskFun converter not found")
        gen.writeString(function(value.toString()))
    }

    companion object {

        private val logger = getLogger()

        fun registerMaskFun(maskType: String, maskFun: (String) -> String) {
            if (maskConverters.containsKey(maskType)) throw ApiException("$maskType already exists")
            synchronized(maskConverters) {
                logger.info("register $maskType convert function")
                maskConverters[maskType] = maskFun
            }
        }
    }
}
