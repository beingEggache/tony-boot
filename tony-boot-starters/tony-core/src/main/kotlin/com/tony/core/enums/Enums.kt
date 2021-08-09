package com.tony.core.enums

import com.fasterxml.jackson.annotation.JsonValue
import java.io.Serializable

sealed interface EnumValue<T : Serializable> {
    @get:JsonValue
    val value: T?
}

interface EnumIntValue : EnumValue<Int>

interface EnumStringValue : EnumValue<String>

private val creators = HashMap<Class<*>, EnumCreator<*, *>>()

@Suppress("UNCHECKED_CAST")
fun <E, KEY> getCreator(clazz: Class<E>): EnumCreator<E, KEY>
    where E : EnumValue<KEY>,
          KEY : Serializable = creators[clazz] as EnumCreator<E, KEY>

abstract class EnumCreator<out E, KEY>(
    private val clazz: Class<out E>
) where E : EnumValue<KEY>,
        KEY : Serializable {

    init {
        if (!clazz.isEnum) throw IllegalStateException("implemented class must be an enum")
        @Suppress("LeakingThis")
        creators[clazz] = this
    }

    companion object {
        const val defaultIntValue = 40404
        const val defaultStringValue = ""
    }

    open fun create(value: KEY) = enumValues.firstOrNull { value == it.value }

    @Suppress("MemberVisibilityCanBePrivate")
    val enumValues: Array<out E> by lazy {
        clazz.enumConstants
    }
}
