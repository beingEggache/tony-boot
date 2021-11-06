package com.tony.enums

import com.fasterxml.jackson.annotation.JsonValue
import java.io.Serializable

sealed interface EnumValue<T : Serializable> {
    @get:JsonValue
    val value: T?
}

interface EnumIntValue : EnumValue<Int>

interface EnumStringValue : EnumValue<String>

abstract class EnumCreator<out E, KEY>(
    private val clazz: Class<out E>
) where E : EnumValue<KEY>,
        KEY : Serializable {

    init {
        if (!clazz.isEnum) throw IllegalStateException("implemented class must be an enum")
        @Suppress("LeakingThis")
        creators[clazz] = this
    }

    open fun create(value: KEY) = enumValues.firstOrNull { value == it.value }

    @Suppress("MemberVisibilityCanBePrivate")
    val enumValues: Array<out E> by lazy {
        clazz.enumConstants
    }

    companion object {

        const val defaultIntValue = 40404

        const val defaultStringValue = ""

        private val creators = HashMap<Class<*>, EnumCreator<*, *>>()

        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        fun <T, R> getCreator(clazz: Class<T>): EnumCreator<T, R>
            where T : EnumValue<R>,
                  R : Serializable = creators[clazz] as EnumCreator<T, R>
    }
}
