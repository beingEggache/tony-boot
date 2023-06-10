package com.tony.web.converter

import com.tony.enums.DEFAULT_INT_VALUE
import com.tony.enums.DEFAULT_STRING_VALUE
import com.tony.enums.EnumCreator
import com.tony.enums.EnumValue
import com.tony.enums.IntEnumValue
import com.tony.enums.StringEnumValue
import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.converter.ConverterFactory
import java.io.Serializable
import java.util.WeakHashMap

/**
 *
 *
 * @author tangli
 * @since 2023/5/25 10:58
 */
private val converters = WeakHashMap<Class<*>, Converter<String, *>>()

/**
 * EnumIntValueConverterFactory
 *
 * @author tangli
 * @since 2023/5/25 10:58
 */
@Suppress("UNCHECKED_CAST")
internal class EnumIntValueConverterFactory :
    ConverterFactory<String, IntEnumValue> {
    override fun <E : IntEnumValue> getConverter(targetType: Class<E>) =
        converters.getOrPut(targetType) { EnumIntValueConverter(targetType) } as Converter<String, E>
}

/**
 * EnumStringValueConverterFactory
 *
 * @author tangli
 * @since 2023/5/25 10:59
 */
@Suppress("UNCHECKED_CAST")
internal class EnumStringValueConverterFactory :
    ConverterFactory<String, StringEnumValue> {
    override fun <E : StringEnumValue> getConverter(targetType: Class<E>) =
        converters.getOrPut(targetType) { EnumStringValueConverter(targetType) } as Converter<String, E>
}

/**
 * EnumValueConverter
 *
 * @author tangli
 * @since 2023/5/25 10:59
 */
internal sealed class EnumValueConverter<out E, K>(enumType: Class<out E>) :
    Converter<String, EnumValue<K>>
    where E : EnumValue<K>,
          K : Serializable {

    private val creator: EnumCreator<E, K> = EnumCreator.creatorOf(enumType)

    protected abstract fun convertSource(source: String): K

    override fun convert(source: String) = creator.create(convertSource(source))
}

/**
 * EnumIntValueConverter
 *
 * @author tangli
 * @since 2023/5/25 10:59
 */
internal class EnumIntValueConverter(enumType: Class<out IntEnumValue>) :
    EnumValueConverter<IntEnumValue, Int>(enumType),
    Converter<String, EnumValue<Int>> {

    override fun convertSource(source: String) = source.toInt()

    override fun convert(source: String): IntEnumValue? {
        return if (source.toInt() == DEFAULT_INT_VALUE) {
            null
        } else {
            super.convert(source)
        }
    }
}

/**
 * EnumStringValueConverter
 *
 * @author tangli
 * @since 2023/5/25 10:59
 */
internal class EnumStringValueConverter(enumType: Class<out StringEnumValue>) :
    EnumValueConverter<StringEnumValue, String>(enumType),
    Converter<String, EnumValue<String>> {

    override fun convertSource(source: String) = source

    override fun convert(source: String): StringEnumValue? =
        if (source == DEFAULT_STRING_VALUE) {
            null
        } else {
            super.convert(source)
        }
}
