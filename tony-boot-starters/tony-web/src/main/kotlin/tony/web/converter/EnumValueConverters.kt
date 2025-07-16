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

package tony.web.converter

/**
 * Enum 转换器
 *
 * 适用于基于 value 的枚举参数自动绑定（如 @RequestParam、@PathVariable）。
 * 要求枚举实现 IntEnumValue/StringEnumValue 并正确实现 EnumCreator。
 * 不支持非 value 枚举或未实现 EnumCreator 的枚举。
 *
 * @author tangli
 * @date 2023/09/28 19:01
 */
import java.io.Serializable
import java.util.concurrent.ConcurrentHashMap
import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.converter.ConverterFactory
import tony.core.enums.DEFAULT_INT_VALUE
import tony.core.enums.DEFAULT_STRING_VALUE
import tony.core.enums.EnumCreator
import tony.core.enums.EnumValue
import tony.core.enums.IntEnumValue
import tony.core.enums.StringEnumValue
import tony.core.utils.asToNotNull

// 枚举类型到参数转换器的全局缓存，保证线程安全，避免重复创建 Converter 实例
private val converters = ConcurrentHashMap<Class<*>, Converter<String, *>>()

/**
 * int型枚举参数转换器工厂
 * 用于将请求参数（String）自动转换为实现 IntEnumValue 的枚举类型
 * 泛型 E：目标枚举类型
 * 缓存命中则复用 Converter，否则新建并缓存
 */
internal class EnumIntValueConverterFactory : ConverterFactory<String, IntEnumValue> {
    @Suppress("UNCHECKED_CAST")
    override fun <E : IntEnumValue> getConverter(targetType: Class<E>): Converter<String, E> =
        converters
            .getOrPut(targetType) {
                EnumIntValueConverter(targetType)
            }.asToNotNull()
}

/**
 * string型枚举参数转换器工厂
 * 用于将请求参数（String）自动转换为实现 StringEnumValue 的枚举类型
 * 泛型 E：目标枚举类型
 * 缓存命中则复用 Converter，否则新建并缓存
 */
internal class EnumStringValueConverterFactory : ConverterFactory<String, StringEnumValue> {
    @Suppress("UNCHECKED_CAST")
    override fun <E : StringEnumValue> getConverter(targetType: Class<E>): Converter<String, E> =
        converters
            .getOrPut(targetType) {
                EnumStringValueConverter(targetType)
            }.asToNotNull()
}

/**
 * 通用枚举参数转换器
 * @param enumType 枚举类型
 * @throws IllegalArgumentException 如果 source 不能正确转换为目标 value 类型
 */
internal sealed class EnumValueConverter<out E, K>(
    enumType: Class<out E>,
) : Converter<String, EnumValue<K>>
    where E : EnumValue<K>,
          K : Serializable {
    private val creator: EnumCreator<E, K> =
        EnumCreator.creatorOf(enumType)

    /**
     * 将 source 字符串转换为 value 类型，子类需实现
     * @throws IllegalArgumentException 如果转换失败
     */
    protected abstract fun convertSource(source: String): K

    /**
     * 将请求参数 source 转换为对应的枚举实例
     * @return 匹配的枚举实例，若找不到则返回 null
     * @throws IllegalArgumentException 如果 source 不能转换为目标类型
     */
    override fun convert(source: String) =
        creator.create(convertSource(source))
}

/**
 * int型枚举参数转换器
 * @param enumType 枚举类型
 * @throws IllegalArgumentException 如果 source 不能转换为 int
 *
 * 特殊约定：当 source 为 DEFAULT_INT_VALUE（-1）时，返回 null
 */
internal class EnumIntValueConverter(
    enumType: Class<out IntEnumValue>,
) : EnumValueConverter<IntEnumValue, Int>(enumType),
    Converter<String, EnumValue<Int>> {
    override fun convertSource(source: String) =
        source.toIntOrNull() ?: throw IllegalArgumentException("Invalid int value for enum: $source")

    override fun convert(source: String): IntEnumValue? =
        if (source.toIntOrNull() == DEFAULT_INT_VALUE) {
            null
        } else {
            super.convert(source)
        }
}

/**
 * string型枚举参数转换器
 * @param enumType 枚举类型
 *
 * 特殊约定：当 source 为 DEFAULT_STRING_VALUE（空字符串）时，返回 null
 */
internal class EnumStringValueConverter(
    enumType: Class<out StringEnumValue>,
) : EnumValueConverter<StringEnumValue, String>(enumType),
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
