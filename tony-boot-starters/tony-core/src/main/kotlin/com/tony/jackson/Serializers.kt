@file:JvmName("Serializers")

package com.tony.jackson

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.tony.exception.ApiException
import kotlin.reflect.KClass
import org.slf4j.LoggerFactory

@JvmSynthetic
internal val maskConverters: MutableMap<Class<*>, MaskConvertFunc> = mutableMapOf(
    MobileMaskFun::class.java to MobileMaskFun(),
    NameMaskFun::class.java to NameMaskFun()
)

/**
 * jackson 序列化字段遮蔽转换.
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
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

/**
 * jackson 字段转换
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
public fun interface MaskConvertFunc {
    public fun convert(input: String?): String?
}

/**
 * 将字符串除开头及结尾部分保留, 其余都用 * 表示
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
public class NameMaskFun : MaskConvertFunc {
    override fun convert(input: String?): String? =
        if (input?.length != null && input.length > 1) {
            input.replaceRange(1 until input.length, "**")
        } else {
            input
        }
}

/**
 * 将手机号除开头 2 位及后 4 位保留,中间用 * 表示
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
public class MobileMaskFun : MaskConvertFunc {
    override fun convert(input: String?): String? =
        if (input?.length != null && input.length >= 4) {
            "${input.substring(0, 2)}****${input.substring(input.length - 4, input.length)}"
        } else {
            input
        }
}

/**
 * jackson 字段遮蔽转换 JsonSerializer
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
public class MaskSerializer : JsonSerializer<Any>() {

    override fun serialize(value: Any, gen: JsonGenerator, serializers: SerializerProvider) {
        val annotation = gen.currentValue::class.java
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
