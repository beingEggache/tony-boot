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

package com.tony.redis

import com.tony.SpringContexts.Env

/**
 * redis key 操作静态单例.
 *
 * @author Tang Li
 * @date 2023/09/28 19:56
 * @since 1.0.0
 */
public data object RedisKeys {
    private val keyPrefix: String by Env.getPropertyByLazy("cache.key-prefix", "")

    /**
     * 生成redis 缓存键名.
     * @param [template] 样板
     * @param [args] args
     * @return [String]
     * @author Tang Li
     * @date 2023/09/28 19:56
     * @since 1.0.0
     */
    @JvmStatic
    public fun genKey(
        template: String,
        vararg args: Any?,
    ): String =
        "${if (keyPrefix.isBlank()) "" else "$keyPrefix:"}${String.format(template, *args)}"
}
