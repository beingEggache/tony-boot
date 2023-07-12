@file:JvmName("ReflectionUtils")

package com.tony.utils

import java.lang.reflect.Field
import java.lang.reflect.Method
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.BeanUtils
import org.springframework.util.ConcurrentReferenceHashMap

/**
 * ReflectionUtils is
 * @author tangli
 * @since 2023/07/12 11:19
 */

internal val getterCache: MutableMap<Field, Method?> = ConcurrentReferenceHashMap()

internal val setterCache: MutableMap<Field, Method?> = ConcurrentReferenceHashMap()

internal val logger: Logger = LoggerFactory.getLogger("com.tony.utils.ReflectionUtils")
public fun Field.getter(): Method? =
    getterCache.getOrPut(this) { BeanUtils.getPropertyDescriptor(declaringClass, name)?.readMethod }

public fun Field.setter(): Method? =
    setterCache.getOrPut(this) { BeanUtils.getPropertyDescriptor(declaringClass, name)?.writeMethod }

public fun Field.setValueFirstUseSetter(obj: Any?, value: Any?) {
    val setter = setter()
    if (setter != null) {
        try {
            setter(obj, value)
        } catch (e: Exception) {
            logger.warn(e.message)
            set(obj, value)
        }
    } else {
        trySetAccessible()
        set(obj, value)
    }
}
