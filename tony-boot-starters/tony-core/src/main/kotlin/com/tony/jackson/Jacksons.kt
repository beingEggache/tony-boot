@file:JvmName("Jacksons")

package com.tony.jackson

/**
 * jackson 相关类
 *
 *
 * @author tangli
 * @since 2022/4/24 16:44
 */
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import com.tony.exception.ApiException
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.util.Date
import java.util.TimeZone
import kotlin.reflect.KClass

@JvmSynthetic
internal val maskConverters: MutableMap<Class<*>, MaskConvertFunc> = mutableMapOf(
    MobileMaskFun::class.java to MobileMaskFun(),
    NameMaskFun::class.java to NameMaskFun(),
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

public fun ObjectMapper.initialize(): ObjectMapper = apply {
    val kotlinModule = KotlinModule.Builder()
        .apply {
            enable(KotlinFeature.NullIsSameAsDefault)
            enable(KotlinFeature.NullToEmptyCollection)
            enable(KotlinFeature.NullToEmptyMap)
        }.build()
    registerModules(kotlinModule, JavaTimeModule(), ParameterNamesModule())
    setTimeZone(TimeZone.getDefault())
    setSerializationInclusion(JsonInclude.Include.ALWAYS)

    configOverride(Date::class.java).format = JsonFormat.Value.forPattern("yyyy-MM-dd HH:mm:ss")
    configOverride(LocalDateTime::class.java).format = JsonFormat.Value.forPattern("yyyy-MM-dd HH:mm:ss")

    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    enable(
        DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE,
        DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS,
        DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
    )
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    @Suppress("DEPRECATION")
    enable(
        JsonGenerator.Feature.IGNORE_UNKNOWN,
        JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN,
        JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS,
        JsonGenerator.Feature.USE_FAST_DOUBLE_WRITER,
    )
    enable(
        JsonParser.Feature.USE_FAST_BIG_NUMBER_PARSER,
        JsonParser.Feature.USE_FAST_DOUBLE_PARSER,
    )
}
