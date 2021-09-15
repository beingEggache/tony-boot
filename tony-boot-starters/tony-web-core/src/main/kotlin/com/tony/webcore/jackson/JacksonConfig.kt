@file:JvmName("JacksonConfig")
@file:Suppress("unused")

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
import com.tony.core.ApiResult
import com.tony.core.exception.ApiException
import com.tony.core.utils.createObjectMapper
import com.tony.core.utils.getLogger
import com.tony.core.utils.isArrayLikeType
import com.tony.core.utils.isBooleanType
import com.tony.core.utils.isDateTimeLikeType
import com.tony.core.utils.isObjLikeType
import com.tony.core.utils.isStringLikeType
import com.tony.webcore.jackson.JacksonConfig.Companion.maskConverters
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import javax.annotation.PostConstruct
import javax.annotation.Resource
import kotlin.reflect.KClass

@Configuration
internal class JacksonConfig {

    @Resource
    lateinit var mappingJackson2HttpMessageConverter: MappingJackson2HttpMessageConverter

    @PostConstruct
    fun init() {
        mappingJackson2HttpMessageConverter.objectMapper = createObjectMapper().apply {
            serializerFactory =
                serializerFactory
                    .withSerializerModifier(NullValueBeanSerializerModifier())
        }
    }

    companion object {

        @JvmSynthetic
        internal val maskConverters: MutableMap<Class<*>, MaskConvertFunc> = mutableMapOf(
            MobileMaskFun::class.java to MobileMaskFun(),
            NameMaskFun::class.java to NameMaskFun()
        )
    }
}

internal class NullArrayJsonSerializer : JsonSerializer<Any?>() {

    override fun serialize(
        value: Any?,
        gen: JsonGenerator,
        serializers: SerializerProvider
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
        serializers: SerializerProvider?
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
        serializers: SerializerProvider
    ) {
        gen.writeString("")
    }
}

class NullValueBeanSerializerModifier : BeanSerializerModifier() {

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
    val value: KClass<out MaskConvertFunc>
) {
    companion object {
        fun getMaskFun(clazz: Class<*>) =
            maskConverters[clazz] ?: throw ApiException("$clazz converter not found")
    }
}

@FunctionalInterface
interface MaskConvertFunc {
    fun convert(input: String?): String?
}

class NameMaskFun : MaskConvertFunc {
    override fun convert(input: String?) =
        if (input?.length != null && input.length > 1)
            input.replaceRange(1 until input.length, "**")
        else input
}

class MobileMaskFun : MaskConvertFunc {
    override fun convert(input: String?) =
        if (input?.length != null && input.length >= 4)
            "${input.substring(0, 2)}****${input.substring(input.length - 4, input.length)}"
        else input
}

class MaskSerializer : JsonSerializer<Any>() {

    override fun serialize(value: Any, gen: JsonGenerator, serializers: SerializerProvider) {
        val annotation = gen.currentValue
            .javaClass
            .getDeclaredField(gen.outputContext.currentName)
            .getAnnotation(MaskConverter::class.java)

        val maskType = annotation.value
        val function = maskConverters[maskType.java] ?: throw ApiException("$maskType converter not found")
        gen.writeString(function.convert(value.toString()))
    }

    companion object {

        @JvmStatic
        private val logger = getLogger()

        fun registerMaskFun(maskType: Class<*>, maskFun: MaskConvertFunc) {
            if (maskConverters.containsKey(maskType)) throw ApiException("$maskType already exists")
            synchronized(maskConverters) {
                logger.info("register $maskType convert function")
                maskConverters[maskType] = maskFun
            }
        }
    }
}
