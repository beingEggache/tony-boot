package com.tony.webcore.converter

import com.tony.core.enums.EnumCreator
import com.tony.core.enums.EnumValue
import com.tony.core.enums.getCreator
import java.io.Serializable
import java.util.WeakHashMap
import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.converter.ConverterFactory

private val converters = WeakHashMap<Class<*>, Converter<String, *>>()

@Suppress("UNCHECKED_CAST")
internal class EnumIntValueConverterFactory
    : ConverterFactory<String, EnumValue<Int>> {
    override fun <E : EnumValue<Int>> getConverter(targetType: Class<E>) =
        converters.getOrPut(targetType) { EnumIntValueConverter(targetType) } as Converter<String, E>
}

@Suppress("UNCHECKED_CAST")
internal class EnumStringValueConverterFactory
    : ConverterFactory<String, EnumValue<String>> {
    override fun <E : EnumValue<String>> getConverter(targetType: Class<E>) =
        converters.getOrPut(targetType) { EnumStringValueConverter(targetType) } as Converter<String, E>
}

internal abstract class EnumValueConverter<out E, K>(enumType: Class<out E>)
    : Converter<String, EnumValue<K>>
    where E : EnumValue<K>,
          K : Serializable {

    private val creator: EnumCreator<E, K> = getCreator(enumType)

    protected abstract fun convertSource(source: String): K

    override fun convert(source: String) = creator.create(convertSource(source))

}

internal class EnumIntValueConverter(enumType: Class<out EnumValue<Int>>)
    : EnumValueConverter<EnumValue<Int>, Int>(enumType),
    Converter<String, EnumValue<Int>> {

    override fun convertSource(source: String) = source.toInt()

    override fun convert(source: String): EnumValue<Int>? {
        val intSource = convertSource(source)
        return if (intSource == EnumCreator.defaultIntEnumValue) null
        else super.convert(source)
    }
}

internal class EnumStringValueConverter(enumType: Class<out EnumValue<String>>)
    : EnumValueConverter<EnumValue<String>, String>(enumType),
    Converter<String, EnumValue<String>> {

    override fun convertSource(source: String) = source

    override fun convert(source: String): EnumValue<String>? {
        val intSource = convertSource(source)
        return if (intSource == EnumCreator.defaultStringValue) null
        else super.convert(source)
    }
}

