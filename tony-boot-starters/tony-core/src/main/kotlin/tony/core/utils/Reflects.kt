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

@file:JvmName("Reflects")

package tony.core.utils
/**
 * 反射工具类
 * @author tangli
 * @date 2023/07/12 19:19
 */
import java.beans.PropertyDescriptor
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Field
import java.lang.reflect.Method
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.BeanUtils
import org.springframework.util.ConcurrentReferenceHashMap

/**
 * getter缓存
 */
@get:JvmSynthetic
internal val getterCache: MutableMap<AnnotatedElement, Method?> = ConcurrentReferenceHashMap()

/**
 * setter缓存
 */
@get:JvmSynthetic
internal val setterCache: MutableMap<AnnotatedElement, Method?> = ConcurrentReferenceHashMap()

@get:JvmSynthetic
internal val logger: Logger = LoggerFactory.getLogger("tony.core.utils.Reflects")

/**
 * PropertyDescriptor
 * @return [PropertyDescriptor]?
 * @author tangli
 * @date 2023/09/13 19:25
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
 * @author tangli
 * @date 2023/09/13 19:26
 */
public fun AnnotatedElement.getter(): Method? =
    getterCache.getOrPut(this) { this.descriptor()?.readMethod }

/**
 * 获取 setter
 * @return [Method]?
 * @author tangli
 * @date 2023/09/13 19:26
 */
public fun AnnotatedElement.setter(): Method? =
    setterCache.getOrPut(this) { this.descriptor()?.writeMethod }

/**
 * 获取字段
 * @return [Field]?
 * @author tangli
 * @date 2023/09/13 19:26
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
 * @author tangli
 * @date 2023/09/13 19:26
 */
public fun AnnotatedElement.setValueFirstUseSetter(
    instance: Any?,
    value: Any?,
) {
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
 * @author tangli
 * @date 2023/09/13 19:27
 */
public fun AnnotatedElement.getValueFirstUseGetter(instance: Any?): Any? {
    if (instance == null) {
        return null
    }

    return getter()?.invoke(instance) ?: field()
        ?.takeIf { it.trySetAccessible() }
        ?.get(instance)
}
