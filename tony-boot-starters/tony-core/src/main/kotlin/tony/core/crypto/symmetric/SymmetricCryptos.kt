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

@file:JvmName("SymmetricCryptos")

package tony.core.crypto.symmetric

/**
 * 对称加密/解密 工具
 * @author tangli
 * @date 2023/05/26 19:58
 */
import tony.core.codec.enums.Encoding
import tony.core.crypto.symmetric.enums.SymmetricCryptoAlgorithm
import tony.core.utils.string

/**
 * 解密为字符串
 * @param [symmetricCryptoAlgorithm] 对称加密算法
 * @param [secret] 秘钥
 * @param [encoding] 二进制编码
 * @return [CharSequence]
 * @author tangli
 * @date 2023/09/27 19:19
 */
public fun CharSequence.decryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    encoding: Encoding,
): CharSequence =
    decryptToBytes(symmetricCryptoAlgorithm, secret.toByteArray(Charsets.UTF_8), encoding).string()

/**
 * 解密为字符串
 * @param [symmetricCryptoAlgorithm] 对称加密算法
 * @param [secret] 秘钥
 * @param [encoding] 二进制编码
 * @return [CharSequence]
 * @author tangli
 * @date 2023/09/27 19:19
 */
public fun CharSequence.decryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    encoding: Encoding,
): CharSequence =
    decryptToBytes(symmetricCryptoAlgorithm, secret, encoding).string()

/**
 * 解密到字节
 * @param [symmetricCryptoAlgorithm] 对称加密算法
 * @param [secret] 秘钥
 * @param [encoding] 二进制编码
 * @return [ByteArray]
 * @author tangli
 * @date 2023/09/27 19:19
 */
public fun CharSequence.decryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    encoding: Encoding,
): ByteArray =
    if (this.isBlank()) {
        ByteArray(0)
    } else {
        symmetricCryptoAlgorithm.algorithm.decrypt(
            toString().toByteArray(Charsets.UTF_8),
            secret.toByteArray(Charsets.UTF_8),
            encoding
        )
    }

/**
 * 解密到字节
 * @param [symmetricCryptoAlgorithm] 对称加密算法
 * @param [secret] 秘钥
 * @param [encoding] 二进制编码
 * @return [ByteArray]
 * @author tangli
 * @date 2023/09/27 19:18
 */
public fun CharSequence.decryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    encoding: Encoding,
): ByteArray =
    if (this.isBlank()) {
        ByteArray(0)
    } else {
        symmetricCryptoAlgorithm.algorithm.decrypt(toString().toByteArray(Charsets.UTF_8), secret, encoding)
    }

/**
 * 解密为字符串
 * @param [symmetricCryptoAlgorithm] 对称加密算法
 * @param [secret] 秘钥
 * @param [encoding] 二进制编码
 * @return [CharSequence]
 * @author tangli
 * @date 2023/09/27 19:17
 */
public fun ByteArray.decryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    encoding: Encoding,
): CharSequence =
    decryptToBytes(symmetricCryptoAlgorithm, secret.toByteArray(Charsets.UTF_8), encoding).string()

/**
 * 解密为字符串
 * @param [symmetricCryptoAlgorithm] 对称加密算法
 * @param [secret] 秘钥
 * @param [encoding] 二进制编码
 * @return [CharSequence]
 * @author tangli
 * @date 2023/09/27 19:17
 */
public fun ByteArray.decryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    encoding: Encoding,
): CharSequence =
    decryptToBytes(symmetricCryptoAlgorithm, secret, encoding).string()

/**
 * 解密到字节
 * @param [symmetricCryptoAlgorithm] 对称加密算法
 * @param [secret] 秘钥
 * @param [encoding] 二进制编码
 * @return [ByteArray]
 * @author tangli
 * @date 2023/09/27 19:17
 */
public fun ByteArray.decryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    encoding: Encoding,
): ByteArray =
    decryptToBytes(symmetricCryptoAlgorithm, secret.toByteArray(Charsets.UTF_8), encoding)

/**
 * 解密到字节
 * @param [symmetricCryptoAlgorithm] 对称加密算法
 * @param [secret] 秘钥
 * @param [encoding] 二进制编码
 * @return [ByteArray]
 * @author tangli
 * @date 2023/09/27 19:17
 */
public fun ByteArray.decryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    encoding: Encoding,
): ByteArray =
    if (this.isEmpty()) {
        this
    } else {
        symmetricCryptoAlgorithm.algorithm.decrypt(this, secret, encoding)
    }

/**
 * 加密为字符串
 * @param [symmetricCryptoAlgorithm] 对称加密算法
 * @param [secret] 秘钥
 * @param [encoding] 二进制编码
 * @return [String]
 * @author tangli
 * @date 2023/09/27 19:16
 */
public fun CharSequence.encryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    encoding: Encoding,
): String =
    encryptToBytes(symmetricCryptoAlgorithm, secret.toByteArray(Charsets.UTF_8), encoding).string()

/**
 * 加密为字符串
 * @param [symmetricCryptoAlgorithm] 对称加密算法
 * @param [secret] 秘钥
 * @param [encoding] 二进制编码
 * @return [CharSequence]
 * @author tangli
 * @date 2023/09/27 19:16
 */
public fun CharSequence.encryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    encoding: Encoding,
): CharSequence =
    encryptToBytes(symmetricCryptoAlgorithm, secret, encoding).string()

/**
 * 加密到字节
 * @param [symmetricCryptoAlgorithm] 对称加密算法
 * @param [secret] 秘钥
 * @param [encoding] 二进制编码
 * @return [ByteArray]
 * @author tangli
 * @date 2023/09/27 19:16
 */
public fun CharSequence.encryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    encoding: Encoding,
): ByteArray =
    if (this.isBlank()) {
        ByteArray(0)
    } else {
        symmetricCryptoAlgorithm.algorithm.encrypt(
            toString().toByteArray(Charsets.UTF_8),
            secret.toByteArray(Charsets.UTF_8),
            encoding
        )
    }

/**
 * 加密到字节
 * @param [symmetricCryptoAlgorithm] 对称加密算法
 * @param [secret] 秘钥
 * @param [encoding] 二进制编码
 * @return [ByteArray]
 * @author tangli
 * @date 2023/09/27 19:17
 */
public fun CharSequence.encryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    encoding: Encoding,
): ByteArray =
    if (this.isBlank()) {
        ByteArray(0)
    } else {
        symmetricCryptoAlgorithm.algorithm.encrypt(toString().toByteArray(Charsets.UTF_8), secret, encoding)
    }

/**
 * 加密为字符串
 * @param [symmetricCryptoAlgorithm] 对称加密算法
 * @param [secret] 秘钥
 * @param [encoding] 二进制编码
 * @return [CharSequence]
 * @author tangli
 * @date 2023/09/27 19:17
 */
public fun ByteArray.encryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    encoding: Encoding,
): CharSequence =
    encryptToBytes(symmetricCryptoAlgorithm, secret.toByteArray(Charsets.UTF_8), encoding).string()

/**
 * 加密为字符串
 * @param [symmetricCryptoAlgorithm] 对称加密算法
 * @param [secret] 秘钥
 * @param [encoding] 二进制编码
 * @return [CharSequence]
 * @author tangli
 * @date 2023/09/27 19:17
 */
public fun ByteArray.encryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    encoding: Encoding,
): CharSequence =
    encryptToBytes(symmetricCryptoAlgorithm, secret, encoding).string()

/**
 * 加密到字节
 * @param [symmetricCryptoAlgorithm] 对称加密算法
 * @param [secret] 秘钥
 * @param [encoding] 二进制编码
 * @return [ByteArray]
 * @author tangli
 * @date 2023/09/27 19:17
 */
public fun ByteArray.encryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    encoding: Encoding,
): ByteArray =
    encryptToBytes(symmetricCryptoAlgorithm, secret.toByteArray(Charsets.UTF_8), encoding)

/**
 * 加密到字节
 * @param [symmetricCryptoAlgorithm] 对称加密算法
 * @param [secret] 秘钥
 * @param [encoding] 二进制编码
 * @return [ByteArray]
 * @author tangli
 * @date 2023/09/27 19:17
 */
public fun ByteArray.encryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    encoding: Encoding,
): ByteArray =
    if (this.isEmpty()) {
        this
    } else {
        symmetricCryptoAlgorithm.algorithm.encrypt(this, secret, encoding)
    }
