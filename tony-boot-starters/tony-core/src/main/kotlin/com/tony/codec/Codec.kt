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
