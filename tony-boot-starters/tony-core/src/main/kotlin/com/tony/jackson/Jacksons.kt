@file:Suppress("unused")

/**
 * tony-dependencies
 * Jacksons
 *
 * TODO
 *
 * @author tangli
 * @since 2022/4/24 16:44
 */
package com.tony.jackson

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.tony.exception.ApiException
import com.tony.utils.getLogger
import kotlin.reflect.KClass

@JvmSynthetic
internal val maskConverters: MutableMap<Class<*>, MaskConvertFunc> = mutableMapOf(
    MobileMaskFun::class.java to MobileMaskFun(),
    NameMaskFun::class.java to NameMaskFun()
)

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
        if (input?.length != null && input.length > 1) {
            input.replaceRange(1 until input.length, "**")
        } else input
}

class MobileMaskFun : MaskConvertFunc {
    override fun convert(input: String?) =
        if (input?.length != null && input.length >= 4) {
            "${input.substring(0, 2)}****${input.substring(input.length - 4, input.length)}"
        } else input
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
                logger.info("Register $maskType convert function.")
                maskConverters[maskType] = maskFun
            }
        }
    }
}
