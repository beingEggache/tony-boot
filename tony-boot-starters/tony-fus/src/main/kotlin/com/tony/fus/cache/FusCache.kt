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

package com.tony.fus.cache

import com.fasterxml.jackson.core.type.TypeReference

/**
 * FusCache is
 * @author tangli
 * @date 2023/10/19 19:41
 * @since 1.0.0
 */
public interface FusCache {
    /**
     * 设值.
     *
     * @param [key] 键
     * @param [value] 值
     * @author Tang Li
     * @date 2023/10/19 19:43
     * @since 1.0.0
     */
    public fun <T : Any> set(
        key: String,
        value: T,
    )

    /**
     * 获值
     * @param [key] 键
     * @param [typeReference] 类型参考
     * @return [T]?
     * @author Tang Li
     * @date 2023/10/19 19:45
     * @since 1.0.0
     */
    public fun <T : Any> get(
        key: String,
        typeReference: TypeReference<T>,
    ): T?

    public fun <T : Any> getOrPut(
        key: String,
        typeReference: TypeReference<T>,
        defaultValue: java.util.function.Supplier<T>,
    ): T

    /**
     * 删.
     * @param [key] 键
     * @return [Long]
     * @author Tang Li
     * @date 2023/10/19 19:45
     * @since 1.0.0
     */
    public fun delete(key: String)
}
