@file:JvmName("ReflectionUtils")

package com.tony.utils

import java.beans.PropertyDescriptor
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Field
import java.lang.reflect.Method
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.BeanUtils
import org.springframework.util.ConcurrentReferenceHashMap

/**
 * ReflectionUtils is
 * @author Tang Li
 * @date 2023/07/12 11:19
 */

internal val getterCache: MutableMap<AnnotatedElement, Method?> = ConcurrentReferenceHashMap()

internal val setterCache: MutableMap<AnnotatedElement, Method?> = ConcurrentReferenceHashMap()

internal val logger: Logger = LoggerFactory.getLogger("com.tony.utils.ReflectionUtils")

/**
 * PropertyDescriptor
 * @return [PropertyDescriptor]?
 * @author Tang Li
 * @date 2023/09/13 10:25
 * @since 1.0.0
 */
public fun AnnotatedElement.descriptor(): PropertyDescriptor? =
    when (this) {
        is Field -> BeanUtils.getPropertyDescriptor(this.declaringClass, this.name)
        is Method -> BeanUtils.findPropertyForMethod(this)
        else -> null
    }

/**
 * 获取 getter
 * @return [Method]?
 * @author Tang Li
 * @date 2023/09/13 10:26
 * @since 1.0.0
 */
public fun AnnotatedElement.getter(): Method? =
    getterCache.getOrPut(this) { this.descriptor()?.readMethod }

/**
 * 获取 setter
 * @return [Method]?
 * @author Tang Li
 * @date 2023/09/13 10:26
 * @since 1.0.0
 */
public fun AnnotatedElement.setter(): Method? =
    setterCache.getOrPut(this) { this.descriptor()?.writeMethod }

/**
 * 获取字段
 * @return [Field]?
 * @author Tang Li
 * @date 2023/09/13 10:26
 * @since 1.0.0
 */
public fun AnnotatedElement.field(): Field? =
    when (this) {
        is Field -> this
        is Method -> descriptor()?.name?.let { declaringClass.getDeclaredField(it) }
        else -> null
    }

/**
 * 设值 优先使用setter
 * @param [instance] 例子
 * @param [value] 价值
 * @author Tang Li
 * @date 2023/09/13 10:26
 * @since 1.0.0
 */
public fun AnnotatedElement.setValueFirstUseSetter(instance: Any?, value: Any?) {
    val setter = setter()
    if (setter != null) {
        try {
            setter(instance, value)
        } catch (e: Exception) {
            logger.warn(e.message)
            field()
                ?.takeIf {
                    it.trySetAccessible()
                }?.set(instance, value)
        }
    } else {
        field()
            ?.takeIf {
                it.trySetAccessible()
            }?.set(instance, value)
    }
}

/**
 * 取值首先使用getter
 * @param [instance]
 * @return [Any]?
 * @author Tang Li
 * @date 2023/09/13 10:27
 * @since 1.0.0
 */
public fun AnnotatedElement.getValueFirstUseGetter(instance: Any?): Any? {
    if (instance == null) {
        return null
    }

    return getter()?.invoke(instance) ?: field()
        ?.takeIf { it.trySetAccessible() }
        ?.get(instance)
}
