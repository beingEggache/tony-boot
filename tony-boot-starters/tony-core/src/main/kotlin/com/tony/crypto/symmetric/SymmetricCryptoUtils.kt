@file:JvmName("SymmetricCryptoUtils")

package com.tony.crypto.symmetric

/**
 * 对称加密/解密 工具
 * @author Tang Li
 * @date 2023/05/26 17:58
 */
import com.tony.codec.enums.Encoding
import com.tony.crypto.symmetric.enums.SymmetricCryptoAlgorithm
import com.tony.utils.string

/**
 * 解密
 * @receiver [CharSequence]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param encoding 二进制编码
 * @return
 */
public fun CharSequence.decryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    encoding: Encoding,
): CharSequence = decryptToBytes(symmetricCryptoAlgorithm, secret.toByteArray(), encoding).string()

/**
 * 解密
 * @receiver [CharSequence]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param encoding 二进制编码
 * @return
 */
public fun CharSequence.decryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    encoding: Encoding,
): CharSequence = decryptToBytes(symmetricCryptoAlgorithm, secret, encoding).string()

/**
 * 解密
 * @receiver [CharSequence]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param encoding 二进制编码
 * @return
 */
public fun CharSequence.decryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    encoding: Encoding,
): ByteArray = if (this.isBlank()) {
    ByteArray(0)
} else {
    symmetricCryptoAlgorithm.algorithm.decrypt(toString().toByteArray(), secret.toByteArray(), encoding)
}

/**
 * 解密
 * @receiver [CharSequence]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param encoding 二进制编码
 * @return
 */
public fun CharSequence.decryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    encoding: Encoding,
): ByteArray = if (this.isBlank()) {
    ByteArray(0)
} else {
    symmetricCryptoAlgorithm.algorithm.decrypt(toString().toByteArray(), secret, encoding)
}

/**
 * 解密
 * @receiver [ByteArray]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param encoding 二进制编码
 * @return
 */
public fun ByteArray.decryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    encoding: Encoding,
): CharSequence = decryptToBytes(symmetricCryptoAlgorithm, secret.toByteArray(), encoding).string()

/**
 * 解密
 * @receiver [ByteArray]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param encoding 二进制编码
 * @return
 */
public fun ByteArray.decryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    encoding: Encoding,
): CharSequence = decryptToBytes(symmetricCryptoAlgorithm, secret, encoding).string()

/**
 * 解密
 * @receiver [ByteArray]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param encoding 二进制编码
 * @return
 */
public fun ByteArray.decryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    encoding: Encoding,
): ByteArray = decryptToBytes(symmetricCryptoAlgorithm, secret.toByteArray(), encoding)

/**
 * 解密
 * @receiver [ByteArray]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param encoding 二进制编码
 * @return
 */
public fun ByteArray.decryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    encoding: Encoding,
): ByteArray = if (this.isEmpty()) {
    this
} else {
    symmetricCryptoAlgorithm.algorithm.decrypt(this, secret, encoding)
}

/**
 * 加密
 * @receiver [CharSequence]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param encoding 二进制编码
 * @return
 */
public fun CharSequence.encryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    encoding: Encoding,
): String = encryptToBytes(symmetricCryptoAlgorithm, secret.toByteArray(), encoding).string()

/**
 * 加密
 * @receiver [CharSequence]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param encoding 二进制编码
 * @return
 */
public fun CharSequence.encryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    encoding: Encoding,
): CharSequence = encryptToBytes(symmetricCryptoAlgorithm, secret, encoding).string()

/**
 * 加密
 * @receiver [CharSequence]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param encoding 二进制编码
 * @return
 */
public fun CharSequence.encryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    encoding: Encoding,
): ByteArray = if (this.isBlank()) {
    ByteArray(0)
} else {
    symmetricCryptoAlgorithm.algorithm.encrypt(toString().toByteArray(), secret.toByteArray(), encoding)
}

/**
 * 加密
 * @receiver [CharSequence]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param encoding 二进制编码
 * @return
 */
public fun CharSequence.encryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    encoding: Encoding,
): ByteArray = if (this.isBlank()) {
    ByteArray(0)
} else {
    symmetricCryptoAlgorithm.algorithm.encrypt(toString().toByteArray(), secret, encoding)
}

/**
 * 加密
 * @receiver [ByteArray]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param encoding 二进制编码
 * @return
 */
public fun ByteArray.encryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    encoding: Encoding,
): CharSequence = encryptToBytes(symmetricCryptoAlgorithm, secret.toByteArray(), encoding).string()

/**
 * 加密
 * @receiver [ByteArray]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param encoding 二进制编码
 * @return
 */
public fun ByteArray.encryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    encoding: Encoding,
): CharSequence = encryptToBytes(symmetricCryptoAlgorithm, secret, encoding).string()

/**
 * 加密
 * @receiver [ByteArray]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param encoding 二进制编码
 * @return
 */
public fun ByteArray.encryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    encoding: Encoding,
): ByteArray = encryptToBytes(symmetricCryptoAlgorithm, secret.toByteArray(), encoding)

/**
 * 加密
 * @receiver [ByteArray]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param encoding 二进制编码
 * @return
 */
public fun ByteArray.encryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    encoding: Encoding,
): ByteArray = if (this.isEmpty()) {
    this
} else {
    symmetricCryptoAlgorithm.algorithm.encrypt(this, secret, encoding)
}
