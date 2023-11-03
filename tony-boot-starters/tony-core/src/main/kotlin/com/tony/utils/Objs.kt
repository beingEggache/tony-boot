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

@file:JvmName("Objs")

package com.tony.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.BeanUtils

/**
 * ObjUtils is
 * @author Tang Li
 * @date 2023/06/13 17:10
 */

/**
 * println
 */
public fun <T> T?.println(): Unit =
    println(this)

/**
 * 获取 logger.
 * @param name logger 名
 */
public fun getLogger(name: String?): Logger =
    LoggerFactory.getLogger(name)

/**
 * 将 [this] 自身 转为 [E] 类型
 *
 * @receiver [Any]?
 * @param E 转换后的类型
 */
@Suppress("UNCHECKED_CAST")
public fun <E> Any?.asTo(): E? where E : Any =
    this as E?

/**
 * 将 [this] 自身 转为 [E] 类型
 *
 * @receiver [Any]
 * @param E 转换后的类型
 */
@Suppress("UNCHECKED_CAST")
public fun <E> Any.asToNotNull(): E where E : Any =
    this as E

@JvmSynthetic
public fun <T> T.getLogger(): Logger where T : Any =
    LoggerFactory.getLogger(this::class.java)

/**
 * 是类型或子类型
 * @param [types] 类型
 * @return [Boolean]
 * @author Tang Li
 * @date 2023/09/13 10:25
 * @since 1.0.0
 */
public fun Any.isTypesOrSubTypesOf(vararg types: Class<*>?): Boolean =
    types.any { this::class.java.isTypeOrSubTypeOf(it) }

/**
 * 复制属性
 * @return [T]
 * @author Tang Li
 * @date 2023/09/25 15:11
 * @since 1.0.0
 * @see BeanUtils.copyProperties
 */
@JvmSynthetic
public inline fun <reified T> Any?.copyTo(): T =
    let {
        val instance = BeanUtils.instantiateClass(T::class.java)
        if (it == null) {
            instance
        } else {
            BeanUtils.copyProperties(it, instance)
            instance
        }
    }

/**
 * 复制属性
 * @param [source] 来源
 * @param [targetType] 目标类型
 * @return [T]
 * @author Tang Li
 * @date 2023/09/25 15:13
 * @since 1.0.0
 */
public fun <T> copyTo(
    source: Any?,
    targetType: Class<T>,
): T {
    val instance = BeanUtils.instantiateClass(targetType)
    if (source == null) {
        return instance
    }
    BeanUtils.copyProperties(source, instance)
    return instance
}

/**
 * 复制属性
 * @param [source] 来源
 * @param [target] 目标
 * @return [T]
 * @author Tang Li
 * @date 2023/09/25 15:13
 * @since 1.0.0
 */
public fun <T> copyTo(
    source: Any?,
    target: T?,
): T? {
    if (source == null || target == null) {
        return target
    }
    BeanUtils.copyProperties(source, target)
    return target
}
