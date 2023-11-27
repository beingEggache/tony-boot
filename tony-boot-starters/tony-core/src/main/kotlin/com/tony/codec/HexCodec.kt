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
import java.util.HexFormat

/**
 * 十六进制编解码器
 * @author Tang Li
 * @date 2023/09/12 17:43
 * @since 1.0.0
 */
public data object HexCodec : Codec {
    @JvmStatic
    private val DIGITS_LOWER = "0123456789abcdef".toCharArray().map { it.code.toByte() }.toByteArray()

    override fun encodeToString(src: ByteArray): String =
        HexFormat
            .of()
            .formatHex(src)

    override fun encodeToString(src: String): String =
        HexFormat
            .of()
            .formatHex(src.toByteArray())

    override fun decodeToByteArray(src: String): ByteArray =
        HexFormat.of().parseHex(src)

    override fun decodeToString(src: String): String =
        decodeToByteArray(src).string()

    override fun decodeToString(src: ByteArray): String =
        HexFormat
            .of()
            .parseHex(src.string())
            .string()

    override fun encodeToByteArray(src: ByteArray): ByteArray {
        val out = ByteArray(src.size shl 1)
        var i = 0
        var j = 0
        while (i < src.size) {
            out[j++] = DIGITS_LOWER[0xF0 and src[i].toInt() ushr 4]
            out[j++] = DIGITS_LOWER[0xF and src[i].toInt()]
            i++
        }
        return out
    }

    override fun decodeToByteArray(src: ByteArray): ByteArray {
        val len = src.size
        require(len and 0x01 == 0) { "hexBinary needs to be even-length: $len" }
        val out = ByteArray(len shr 1)
        var i = 0
        var j = 0
        while (j < len) {
            var f = Character.digit(src[j].toInt(), 16) shl 4
            j++
            f = f or Character.digit(src[j].toInt(), 16)
            j++
            out[i] = (f and 0xFF).toByte()
            i++
        }
        return out
    }
}
