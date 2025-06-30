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

package tony.crypto.symmetric

import javax.crypto.Cipher
import tony.codec.enums.Encoding

/**
 * 对称 加密/解密
 * @author tangli
 * @date 2023/09/27 19:20
 */
public sealed interface SymmetricCrypto {
    /**
     * 解密
     * @param [src] 待解密
     * @param [secret] 秘钥
     * @param [encoding] 编码
     * @return [ByteArray]
     * @author tangli
     * @date 2023/09/27 19:20
     */
    public fun decrypt(
        src: ByteArray,
        secret: ByteArray,
        encoding: Encoding,
    ): ByteArray {
        val digestedSrc = encoding.codec.decodeToByteArray(src)
        return decrypt(digestedSrc, secret)
    }

    /**
     * 解密
     * @param [src] 待解密
     * @param [secret] 秘钥
     * @return [ByteArray]
     * @author tangli
     * @date 2023/09/27 19:20
     */
    public fun decrypt(
        src: ByteArray,
        secret: ByteArray,
    ): ByteArray =
        crypto(src, secret, Cipher.DECRYPT_MODE)

    /**
     * 加密
     * @param [src] 待加密
     * @param [secret] 秘钥
     * @param [encoding] 编码
     * @return [ByteArray]
     * @author tangli
     * @date 2023/09/27 19:20
     */
    public fun encrypt(
        src: ByteArray,
        secret: ByteArray,
        encoding: Encoding,
    ): ByteArray =
        encrypt(src, secret)
            .run {
                encoding.codec.encodeToByteArray(this)
            }

    /**
     * 加密
     * @param [src] 待加密
     * @param [secret] 秘钥
     * @return [ByteArray]
     * @author tangli
     * @date 2023/09/27 19:20
     */
    public fun encrypt(
        src: ByteArray,
        secret: ByteArray,
    ): ByteArray =
        crypto(src, secret, Cipher.ENCRYPT_MODE)

    /**
     * 对称加密解密
     * @param [src] 待加密/解密
     * @param [secret] 秘钥
     * @param [mode] 加密/解密 [Cipher.ENCRYPT_MODE]/[Cipher.DECRYPT_MODE]
     * @return [ByteArray]
     * @author tangli
     * @date 2023/09/27 19:20
     */
    public fun crypto(
        src: ByteArray,
        secret: ByteArray,
        mode: Int,
    ): ByteArray
}
