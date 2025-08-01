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

package tony.core.codec

import java.util.HexFormat
import tony.core.utils.string

/**
 * 十六进制编解码器
 * @author tangli
 * @date 2023/09/12 19:43
 */
public data object HexCodec : Codec {
    override fun encodeToString(src: ByteArray): String =
        HexFormat
            .of()
            .formatHex(src)

    override fun encodeToString(src: CharSequence): String =
        HexFormat
            .of()
            .formatHex(src.toString().toByteArray(Charsets.UTF_8))

    override fun decodeToByteArray(src: CharSequence): ByteArray =
        HexFormat.of().parseHex(src)

    override fun decodeToString(src: CharSequence): String =
        decodeToByteArray(src).string()

    override fun decodeToString(src: ByteArray): String =
        HexFormat
            .of()
            .parseHex(src.string())
            .string()

    override fun encodeToByteArray(src: ByteArray): ByteArray =
        HexFormat
            .of()
            .formatHex(src)
            .toByteArray()

    override fun decodeToByteArray(src: ByteArray): ByteArray =
        HexFormat
            .of()
            .parseHex(src.string())
}
