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

@file:JvmName("Codecs")

package com.tony.codec
/**
 * 二进制编码工具类
 * @author tangli
 * @date 2023/09/12 19:43
 * @since 1.0.0
 */
import com.tony.codec.enums.Encoding

/**
 * 编码为字符串
 * @param [encoding] 编码
 * @return [String]
 * @author tangli
 * @date 2023/09/12 19:43
 * @since 1.0.0
 */
public fun CharSequence.encodeToString(encoding: Encoding): String =
    encoding.codec.encodeToString(this)

/**
 * 编码到字节数组
 * @param [encoding] 编码
 * @return [ByteArray]
 * @author tangli
 * @date 2023/09/12 19:43
 * @since 1.0.0
 */
public fun CharSequence.encodeToByteArray(encoding: Encoding): ByteArray =
    encoding.codec.encodeToByteArray(this)

/**
 * 编码为字符串
 * @param [encoding] 编码
 * @return [String]
 * @author tangli
 * @date 2023/09/12 19:44
 * @since 1.0.0
 */
public fun ByteArray.encodeToString(encoding: Encoding): String =
    encoding.codec.encodeToString(this)

/**
 * 编码到字节数组
 * @param [encoding] 编码
 * @return [ByteArray]
 * @author tangli
 * @date 2023/09/12 19:44
 * @since 1.0.0
 */
public fun ByteArray.encodeToByteArray(encoding: Encoding): ByteArray =
    encoding.codec.decodeToByteArray(this)

/**
 * 解码为字符串
 * @param [encoding] 编码
 * @return [String]
 * @author tangli
 * @date 2023/09/12 19:44
 * @since 1.0.0
 */
public fun CharSequence.decodeToString(encoding: Encoding): String =
    encoding.codec.decodeToString(this)

/**
 * 解码为字节数组
 * @param [encoding] 编码
 * @return [ByteArray]
 * @author tangli
 * @date 2023/09/12 19:44
 * @since 1.0.0
 */
public fun CharSequence.decodeToByteArray(encoding: Encoding): ByteArray =
    encoding.codec.decodeToByteArray(this)

/**
 * 解码为字符串
 * @param [encoding] 编码
 * @return [String]
 * @author tangli
 * @date 2023/09/12 19:44
 * @since 1.0.0
 */
public fun ByteArray.decodeToString(encoding: Encoding): String =
    encoding.codec.decodeToString(this)

/**
 * 解码为字节数组
 * @param [encoding] 编码
 * @return [ByteArray]
 * @author tangli
 * @date 2023/09/12 19:44
 * @since 1.0.0
 */
public fun ByteArray.decodeToByteArray(encoding: Encoding): ByteArray =
    encoding.codec.decodeToByteArray(this)
