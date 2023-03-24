package com.tony.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.tony.utils.getLogger
import java.io.Serializable

public sealed interface EnumValue<T : Serializable> {
    @get:JsonValue
    public val value: T?
}

public interface EnumIntValue : EnumValue<Int>

public interface EnumStringValue : EnumValue<String>

public abstract class EnumCreator<out E, KEY>(
    private val clazz: Class<out E>,
) where E : EnumValue<KEY>,
      KEY : Serializable {

    private val logger = getLogger()

    init {
        if (!clazz.isEnum) throw IllegalStateException("implemented class must be an enum")
        @Suppress("LeakingThis")
        creators[clazz] = this
        logger.debug("${ clazz.name } EnumCreator initialized.")
    }

    public open fun create(value: KEY): E? = enumValues.firstOrNull { value == it.value }

    @Suppress("MemberVisibilityCanBePrivate")
    public val enumValues: Array<out E> by lazy {
        clazz.enumConstants
    }

    public companion object {

        public const val defaultIntValue: Int = 40404

        public const val defaultStringValue: String = ""

        private val creators = HashMap<Class<*>, EnumCreator<*, *>>()

        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        public fun <T, R> getCreator(clazz: Class<T>): EnumCreator<T, R>
            where T : EnumValue<R>,
                  R : Serializable = creators[clazz] as EnumCreator<T, R>
    }
}
