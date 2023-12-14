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

@file:JvmName("Logs")

package com.tony.utils
/**
 * 日志工具类
 * @author Tang Li
 * @date 2023/09/13 19:22
 * @since 1.0.0
 */
import org.slf4j.MDC

/**
 * mdc放入或获取默认值
 * @param [key] 钥匙
 * @param [default] 违约
 * @param [source] 来源
 * @return [String]
 * @author Tang Li
 * @date 2023/09/27 19:02
 * @since 1.0.0
 */
public fun mdcPutOrGetDefault(
    key: String,
    default: String = uuid(),
    source: (() -> String?)? = null,
): String {
    val value = source?.invoke().ifNullOrBlank(MDC.get(key).ifNullOrBlank(default))
    MDC.put(key, value)
    return value
}
