package com.tony.enums

import com.fasterxml.jackson.annotation.JsonValue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Serializable

public sealed interface EnumValue<T : Serializable> {
    @get:JsonValue
    public val value: T?
}

public interface EnumIntValue : EnumValue<Int>

public interface EnumStringValue : EnumValue<String>

public abstract class EnumCreator<out E, KEY>(
    private val clazz: Class<out E>,
) where E : EnumValue<KEY>, KEY : Serializable {
    public open fun create(value: KEY): E? = enumValues.firstOrNull { value == it.value }

    private val enumValues: Array<out E> by lazy {
        clazz.enumConstants
    }

    public companion object EnumCreatorFactory {

        public const val defaultIntValue: Int = 40404
        public const val defaultStringValue: String = ""
        private val creators = HashMap<Class<*>, EnumCreator<*, *>>()
        private val logger: Logger = LoggerFactory.getLogger(EnumCreatorFactory::class.java)

        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        public fun <T, R> getCreator(clazz: Class<T>): EnumCreator<T, R> where T : EnumValue<R>, R : Serializable {
            return creators.getOrPut(clazz) {
                logger.debug("${clazz.name} EnumCreator initialized.")
                clazz
                    .classes
                    .firstOrNull {
                        EnumCreator::class.java.isAssignableFrom(it)
                    }
                    ?.constructors
                    ?.firstOrNull()
                    ?.newInstance(null) as EnumCreator<T, R>
            } as EnumCreator<T, R>
        }
    }
}
