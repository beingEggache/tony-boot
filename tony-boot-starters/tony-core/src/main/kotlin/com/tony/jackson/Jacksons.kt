/**
 * Jacksons
 *
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
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

@JvmSynthetic
internal val maskConverters: MutableMap<Class<*>, MaskConvertFunc> = mutableMapOf(
    MobileMaskFun::class.java to MobileMaskFun(),
    NameMaskFun::class.java to NameMaskFun(),
)

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = MaskSerializer::class)
public annotation class MaskConverter(
    val value: KClass<out MaskConvertFunc>,
) {
    public companion object {
        public fun getMaskFun(clazz: Class<*>): MaskConvertFunc =
            maskConverters[clazz] ?: throw ApiException("$clazz converter not found")
    }
}

public fun interface MaskConvertFunc {
    public fun convert(input: String?): String?
}

public class NameMaskFun : MaskConvertFunc {
    override fun convert(input: String?): String? =
        if (input?.length != null && input.length > 1) {
            input.replaceRange(1 until input.length, "**")
        } else {
            input
        }
}

public class MobileMaskFun : MaskConvertFunc {
    override fun convert(input: String?): String? =
        if (input?.length != null && input.length >= 4) {
            "${input.substring(0, 2)}****${input.substring(input.length - 4, input.length)}"
        } else {
            input
        }
}

public class MaskSerializer : JsonSerializer<Any>() {

    override fun serialize(value: Any, gen: JsonGenerator, serializers: SerializerProvider) {
        val annotation = gen.currentValue
            .javaClass
            .getDeclaredField(gen.outputContext.currentName)
            .getAnnotation(MaskConverter::class.java)

        val maskType = annotation.value
        val function = maskConverters[maskType.java] ?: throw ApiException("$maskType converter not found")
        gen.writeString(function.convert(value.toString()))
    }

    public companion object {

        @JvmStatic
        private val logger = LoggerFactory.getLogger(MaskSerializer::class.java)

        public fun registerMaskFun(maskType: Class<*>, maskFun: MaskConvertFunc) {
            if (maskConverters.containsKey(maskType)) throw ApiException("$maskType already exists")
            synchronized(maskConverters) {
                logger.info("Register $maskType convert function.")
                maskConverters[maskType] = maskFun
            }
        }
    }
}
