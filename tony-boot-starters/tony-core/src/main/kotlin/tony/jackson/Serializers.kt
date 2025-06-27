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

@file:JvmName("Serializers")

/**
 * jackson Serializers
 * @author tangli
 * @date 2023/09/13 19:18
 */

package tony.jackson

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import kotlin.reflect.KClass
import org.slf4j.LoggerFactory
import tony.exception.ApiException

/**
 * 掩模转换器
 */
@get:JvmSynthetic
internal val maskConverters: MutableMap<Class<*>?, MaskConvertFunc> =
    mutableMapOf(
        MobileMaskFun::class.java to MobileMaskFun(),
        NameMaskFun::class.java to NameMaskFun()
    )

/**
 * jackson 序列化字段遮蔽转换.
 *
 * @author tangli
 * @date 2021/12/6 10:51
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
 * @date 2021/12/6 10:51
 */
public fun interface MaskConvertFunc {
    public fun convert(input: String?): String?
}

/**
 * 将字符串除开头及结尾部分保留, 其余都用 * 表示
 *
 * @author tangli
 * @date 2021/12/6 10:51
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
 * @date 2021/12/6 10:51
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
 * @date 2021/12/6 10:51
 */
public class MaskSerializer : JsonSerializer<Any>() {
    override fun serialize(
        value: Any,
        gen: JsonGenerator,
        serializers: SerializerProvider,
    ) {
        val annotation =
            gen.currentValue()::class.java
                .getDeclaredField(gen.outputContext.currentName)
                .getAnnotation(MaskConverter::class.java)

        val maskType = annotation?.value?.java
        val function = maskConverters[maskType] ?: throw ApiException("$maskType converter not found")
        gen.writeString(function.convert(value.toString()))
    }

    public companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(MaskSerializer::class.java)

        public fun registerMaskFun(
            maskType: Class<*>,
            maskFun: MaskConvertFunc,
        ) {
            if (maskConverters.containsKey(maskType)) throw ApiException("$maskType already exists")
            synchronized(maskConverters) {
                logger.info("Register $maskType convert function.")
                maskConverters[maskType] = maskFun
            }
        }
    }
}
