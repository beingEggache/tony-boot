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

package tony.core.utils

import com.fasterxml.jackson.module.kotlin.convertValue

/**
 * ObjUtils is
 * @author tangli
 * @date 2023/06/13 19:10
 */

/**
 * println
 */
@JvmSynthetic
public fun <T> T?.println(): Unit =
    println(this)

/**
 * 将 [this] 自身 转为 [E] 类型
 * @receiver [Any]?
 * @param [E] 目标类型
 * @return [E]?
 * @author tangli
 * @date 2024/02/06 13:56
 */
@Suppress("UNCHECKED_CAST")
public fun <E> Any?.asTo(): E? where E : Any =
    this as? E

/**
 * 将 [this] 自身 转为 [E] 类型
 * @receiver [Any]?
 * @param [E] 目标类型
 * @param default 默认值
 * @return [E]
 * @author tangli
 * @date 2024/02/06 13:57
 */
@Suppress("UNCHECKED_CAST")
public fun <E> Any?.asToDefault(default: E): E where E : Any =
    this as? E ?: default

/**
 * 将 [this] 自身 转为 [E] 非空类型
 *
 * @receiver [Any]
 * @param [E] 目标类型
 * @author tangli
 * @date 2024/02/06 13:57
 */
@Suppress("UNCHECKED_CAST")
public fun <E> Any.asToNotNull(): E where E : Any =
    this as E

/**
 * 是类型或子类型
 * @param [types] 类型
 * @return [Boolean]
 * @author tangli
 * @date 2023/09/13 19:25
 */
public fun Any.isTypesOrSubTypesOf(vararg types: Class<*>?): Boolean =
    types.any { this::class.java.isTypeOrSubTypeOf(it) }

/**
 * 复制属性
 * @return [T]
 * @author tangli
 * @date 2023/09/25 19:11
 * @see [com.fasterxml.jackson.databind.ObjectMapper.convertValue]
 */
@JvmSynthetic
public inline fun <reified T> Any?.copyTo(): T =
    globalObjectMapper.convertValue(this, T::class.java)

/**
 * 复制属性
 * @receiver 来源
 * @param [targetType] 目标类型
 * @return [T]
 * @author tangli
 * @date 2023/09/25 19:13
 * @see [com.fasterxml.jackson.databind.ObjectMapper.convertValue]
 */
public fun <T> Any?.copyTo(targetType: Class<T>): T =
    globalObjectMapper.convertValue(this, targetType)

/**
 * 普通对象非null, 字符串非blank
 * @return [Boolean]
 * @author tangli
 * @date 2024/01/15 10:13
 */
public fun Any?.notBlank(): Boolean =
    if (this == null) {
        false
    } else if (this is CharSequence) {
        isNotBlank()
    } else {
        true
    }
