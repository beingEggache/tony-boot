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

package com.tony.web.converter

/**
 * Enum 转换器
 * @author Tang Li
 * @date 2023/09/28 11:01
 * @since 1.0.0
 */
import com.tony.enums.DEFAULT_INT_VALUE
import com.tony.enums.DEFAULT_STRING_VALUE
import com.tony.enums.EnumCreator
import com.tony.enums.EnumValue
import com.tony.enums.IntEnumValue
import com.tony.enums.StringEnumValue
import java.io.Serializable
import java.util.WeakHashMap
import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.converter.ConverterFactory

private val converters =
    WeakHashMap<Class<*>, Converter<String, *>>()

/**
 * enum-int值转换器工厂
 * @author Tang Li
 * @date 2023/09/28 11:01
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
internal class EnumIntValueConverterFactory :
    ConverterFactory<String, IntEnumValue> {
    override fun <E : IntEnumValue> getConverter(targetType: Class<E>) =
        converters
            .getOrPut(targetType) {
                EnumIntValueConverter(targetType)
            } as Converter<String, E>
}

/**
 * 枚举 - 字符串 转换器工厂
 * @author Tang Li
 * @date 2023/09/28 11:02
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
internal class EnumStringValueConverterFactory :
    ConverterFactory<String, StringEnumValue> {
    override fun <E : StringEnumValue> getConverter(targetType: Class<E>) =
        converters
            .getOrPut(targetType) {
                EnumStringValueConverter(targetType)
            } as Converter<String, E>
}

/**
 * EnumValueConverter
 *
 * @author Tang Li
 * @date 2023/5/25 10:59
 */
internal sealed class EnumValueConverter<out E, K>(enumType: Class<out E>) :
    Converter<String, EnumValue<K>>
    where E : EnumValue<K>,
          K : Serializable {
    private val creator: EnumCreator<E, K> =
        EnumCreator.creatorOf(enumType)

    protected abstract fun convertSource(source: String): K

    override fun convert(source: String) =
        creator.create(convertSource(source))
}

/**
 * EnumIntValueConverter
 *
 * @author Tang Li
 * @date 2023/5/25 10:59
 */
internal class EnumIntValueConverter(enumType: Class<out IntEnumValue>) :
    EnumValueConverter<IntEnumValue, Int>(enumType),
    Converter<String, EnumValue<Int>> {
    override fun convertSource(source: String) =
        source.toInt()

    override fun convert(source: String): IntEnumValue? =
        if (source.toInt() == DEFAULT_INT_VALUE) {
            null
        } else {
            super.convert(source)
        }
}

/**
 * EnumStringValueConverter
 *
 * @author Tang Li
 * @date 2023/5/25 10:59
 */
internal class EnumStringValueConverter(enumType: Class<out StringEnumValue>) :
    EnumValueConverter<StringEnumValue, String>(enumType),
    Converter<String, EnumValue<String>> {
    override fun convertSource(source: String) =
        source

    override fun convert(source: String): StringEnumValue? =
        if (source == DEFAULT_STRING_VALUE) {
            null
        } else {
            super.convert(source)
        }
}
