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

package com.tony.codec

import com.tony.utils.string

/**
 * 编解码器
 * @author Tang Li
 * @date 2023/09/12 17:45
 * @since 1.0.0
 */
public sealed interface Codec {
    /**
     * 编码为字符串
     * @param [src] src
     * @return [String]
     * @author Tang Li
     * @date 2023/09/12 17:45
     * @since 1.0.0
     */
    public fun encodeToString(src: String): String = encodeToByteArray(src).string()

    /**
     * 编码为字符串
     * @param [src] src
     * @return [String]
     * @author Tang Li
     * @date 2023/09/12 17:45
     * @since 1.0.0
     */
    public fun encodeToString(src: ByteArray): String = encodeToByteArray(src).string()

    /**
     * 编码到字节数组
     * @param [src] src
     * @return [ByteArray]
     * @author Tang Li
     * @date 2023/09/12 17:45
     * @since 1.0.0
     */
    public fun encodeToByteArray(src: String): ByteArray {
        return encodeToByteArray(src.toByteArray())
    }

    /**
     * 编码到字节数组
     * @param [src] src
     * @return [ByteArray]
     * @author Tang Li
     * @date 2023/09/12 17:45
     * @since 1.0.0
     */
    public fun encodeToByteArray(src: ByteArray): ByteArray

    /**
     * 解码为字符串
     * @param [src] src
     * @return [String]
     * @author Tang Li
     * @date 2023/09/12 17:45
     * @since 1.0.0
     */
    public fun decodeToString(src: String): String = decodeToByteArray(src).string()

    /**
     * 解码为字符串
     * @param [src] src
     * @return [String]
     * @author Tang Li
     * @date 2023/09/12 17:45
     * @since 1.0.0
     */
    public fun decodeToString(src: ByteArray): String = decodeToByteArray(src).string()

    /**
     * 解码为字节数组
     * @param [src] src
     * @return [ByteArray]
     * @author Tang Li
     * @date 2023/09/12 17:45
     * @since 1.0.0
     */
    public fun decodeToByteArray(src: String): ByteArray {
        return decodeToByteArray(src.toByteArray())
    }

    /**
     * 解码为字节数组
     * @param [src] src
     * @return [ByteArray]
     * @author Tang Li
     * @date 2023/09/12 17:45
     * @since 1.0.0
     */
    public fun decodeToByteArray(src: ByteArray): ByteArray
}
