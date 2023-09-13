@file:JvmName("CodecUtils")

package com.tony.codec

import com.tony.codec.enums.Encoding

/**
 * 编码为字符串
 * @param [encoding] 编码
 * @return [String]
 * @author Tang Li
 * @date 2023/09/12 17:43
 * @since 1.0.0
 */
public fun String.encodeToString(encoding: Encoding): String = encoding.codec.encodeToString(this)

/**
 * 编码到字节数组
 * @param [encoding] 编码
 * @return [ByteArray]
 * @author Tang Li
 * @date 2023/09/12 17:43
 * @since 1.0.0
 */
public fun String.encodeToByteArray(encoding: Encoding): ByteArray = encoding.codec.encodeToByteArray(this)

/**
 * 编码为字符串
 * @param [encoding] 编码
 * @return [String]
 * @author Tang Li
 * @date 2023/09/12 17:44
 * @since 1.0.0
 */
public fun ByteArray.encodeToString(encoding: Encoding): String = encoding.codec.encodeToString(this)

/**
 * 编码到字节数组
 * @param [encoding] 编码
 * @return [ByteArray]
 * @author Tang Li
 * @date 2023/09/12 17:44
 * @since 1.0.0
 */
public fun ByteArray.encodeToByteArray(encoding: Encoding): ByteArray = encoding.codec.decodeToByteArray(this)

/**
 * 解码为字符串
 * @param [encoding] 编码
 * @return [String]
 * @author Tang Li
 * @date 2023/09/12 17:44
 * @since 1.0.0
 */
public fun String.decodeToString(encoding: Encoding): String = encoding.codec.decodeToString(this)

/**
 * 解码为字节数组
 * @param [encoding] 编码
 * @return [ByteArray]
 * @author Tang Li
 * @date 2023/09/12 17:44
 * @since 1.0.0
 */
public fun String.decodeToByteArray(encoding: Encoding): ByteArray = encoding.codec.decodeToByteArray(this)

/**
 * 解码为字符串
 * @param [encoding] 编码
 * @return [String]
 * @author Tang Li
 * @date 2023/09/12 17:44
 * @since 1.0.0
 */
public fun ByteArray.decodeToString(encoding: Encoding): String = encoding.codec.decodeToString(this)

/**
 * 解码为字节数组
 * @param [encoding] 编码
 * @return [ByteArray]
 * @author Tang Li
 * @date 2023/09/12 17:44
 * @since 1.0.0
 */
public fun ByteArray.decodeToByteArray(encoding: Encoding): ByteArray = encoding.codec.decodeToByteArray(this)
