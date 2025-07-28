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

@file:JvmName("MonoValues")

package tony.core.model

import java.time.temporal.Temporal
import tony.core.utils.asToNotNull

/**
 * 包装成简单值统一结构.
 * @return [MonoValue]
 * @author tangli
 * @date 2025/07/17 10:21
 */
public fun Boolean.wrap(): MonoValue<Boolean> =
    MonoValueImpl(this)

/**
 * 包装成简单值统一结构.
 * @return [MonoValue]
 * @author tangli
 * @date 2025/07/17 10:21
 */
public fun String.wrap(): MonoValue<String> =
    MonoValueImpl(this)

/**
 * 包装成简单值统一结构.
 * @return [MonoValue]
 * @author tangli
 * @date 2025/07/17 10:21
 */
public fun <E : Number> E.wrap(): MonoValue<E> =
    MonoValueImpl(this)

/**
 * 包装成简单值统一结构.
 * @return [MonoValue]
 * @author tangli
 * @date 2025/07/17 10:21
 */
public fun <E : Enum<*>> E.wrap(): MonoValue<E> =
    MonoValueImpl(this)

/**
 * 包装成简单值统一结构.
 * @return [Temporal]
 * @author tangli
 * @date 2025/07/17 10:21
 */
public fun <E : Temporal> E.wrap(): MonoValue<E> =
    MonoValueImpl(this)

/**
 * 包装成简单值统一结构.
 * @return [MonoValue]<[E]>
 * @author tangli
 * @date 2025/07/28 17:06
 */
public fun <E> E?.wrap(): MonoValue<E> {
    if (this == null) {
        return MonoValueImpl(null)
    }
    return when (this) {
        is Boolean -> this.wrap()
        is Number -> this.wrap()
        is String -> this.wrap()
        is Enum<*> -> this.wrap()
        is Temporal -> this.wrap()
        else -> throw IllegalArgumentException("Unsupported type: $this")
    }.asToNotNull()
}
