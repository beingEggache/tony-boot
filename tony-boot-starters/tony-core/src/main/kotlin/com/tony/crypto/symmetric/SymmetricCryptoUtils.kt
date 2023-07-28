@file:JvmName("SymmetricCryptoUtils")

package com.tony.crypto.symmetric

/**
 * 对称加密/解密 工具
 * @author tangli
 * @since 2023/05/26 17:58
 */
import com.tony.crypto.symmetric.enums.CryptoEncoding
import com.tony.crypto.symmetric.enums.SymmetricCryptoAlgorithm

/**
 * 解密
 * @receiver [CharSequence]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoEncoding 二进制编码
 * @return
 */
public fun CharSequence.decryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    cryptoEncoding: CryptoEncoding,
): String = decryptToBytes(symmetricCryptoAlgorithm, secret.toByteArray(), cryptoEncoding)
    .toString(Charsets.UTF_8)

/**
 * 解密
 * @receiver [CharSequence]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoEncoding 二进制编码
 * @return
 */
public fun CharSequence.decryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    cryptoEncoding: CryptoEncoding,
): String = decryptToBytes(symmetricCryptoAlgorithm, secret, cryptoEncoding)
    .toString(Charsets.UTF_8)

/**
 * 解密
 * @receiver [CharSequence]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoEncoding 二进制编码
 * @return
 */
public fun CharSequence.decryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    cryptoEncoding: CryptoEncoding,
): ByteArray = if (this.isBlank()) {
    ByteArray(0)
} else {
    symmetricCryptoAlgorithm.algorithm.decrypt(toString().toByteArray(), secret.toByteArray(), cryptoEncoding)
}

/**
 * 解密
 * @receiver [CharSequence]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoEncoding 二进制编码
 * @return
 */
public fun CharSequence.decryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    cryptoEncoding: CryptoEncoding,
): ByteArray = if (this.isBlank()) {
    ByteArray(0)
} else {
    symmetricCryptoAlgorithm.algorithm.decrypt(toString().toByteArray(), secret, cryptoEncoding)
}

/**
 * 解密
 * @receiver [ByteArray]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoEncoding 二进制编码
 * @return
 */
public fun ByteArray.decryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    cryptoEncoding: CryptoEncoding,
): String = decryptToBytes(symmetricCryptoAlgorithm, secret.toByteArray(), cryptoEncoding)
    .toString(Charsets.UTF_8)

/**
 * 解密
 * @receiver [ByteArray]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoEncoding 二进制编码
 * @return
 */
public fun ByteArray.decryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    cryptoEncoding: CryptoEncoding,
): String = decryptToBytes(symmetricCryptoAlgorithm, secret, cryptoEncoding)
    .toString(Charsets.UTF_8)

/**
 * 解密
 * @receiver [ByteArray]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoEncoding 二进制编码
 * @return
 */
public fun ByteArray.decryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    cryptoEncoding: CryptoEncoding,
): ByteArray = decryptToBytes(symmetricCryptoAlgorithm, secret.toByteArray(), cryptoEncoding)

/**
 * 解密
 * @receiver [ByteArray]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoEncoding 二进制编码
 * @return
 */
public fun ByteArray.decryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    cryptoEncoding: CryptoEncoding,
): ByteArray = if (this.isEmpty()) {
    this
} else {
    symmetricCryptoAlgorithm.algorithm.decrypt(this, secret, cryptoEncoding)
}

/**
 * 加密
 * @receiver [CharSequence]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoEncoding 二进制编码
 * @return
 */
public fun CharSequence.encryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    cryptoEncoding: CryptoEncoding,
): String = encryptToBytes(symmetricCryptoAlgorithm, secret.toByteArray(), cryptoEncoding)
    .toString(Charsets.UTF_8)

/**
 * 加密
 * @receiver [CharSequence]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoEncoding 二进制编码
 * @return
 */
public fun CharSequence.encryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    cryptoEncoding: CryptoEncoding,
): String = encryptToBytes(symmetricCryptoAlgorithm, secret, cryptoEncoding)
    .toString(Charsets.UTF_8)

/**
 * 加密
 * @receiver [CharSequence]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoEncoding 二进制编码
 * @return
 */
public fun CharSequence.encryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    cryptoEncoding: CryptoEncoding,
): ByteArray = if (this.isBlank()) {
    ByteArray(0)
} else {
    symmetricCryptoAlgorithm.algorithm.encrypt(toString().toByteArray(), secret.toByteArray(), cryptoEncoding)
}

/**
 * 加密
 * @receiver [CharSequence]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoEncoding 二进制编码
 * @return
 */
public fun CharSequence.encryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    cryptoEncoding: CryptoEncoding,
): ByteArray = if (this.isBlank()) {
    ByteArray(0)
} else {
    symmetricCryptoAlgorithm.algorithm.encrypt(toString().toByteArray(), secret, cryptoEncoding)
}

/**
 * 加密
 * @receiver [ByteArray]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoEncoding 二进制编码
 * @return
 */
public fun ByteArray.encryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    cryptoEncoding: CryptoEncoding,
): String = encryptToBytes(symmetricCryptoAlgorithm, secret.toByteArray(), cryptoEncoding)
    .toString(Charsets.UTF_8)

/**
 * 加密
 * @receiver [ByteArray]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoEncoding 二进制编码
 * @return
 */
public fun ByteArray.encryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    cryptoEncoding: CryptoEncoding,
): String = encryptToBytes(symmetricCryptoAlgorithm, secret, cryptoEncoding)
    .toString(Charsets.UTF_8)

/**
 * 加密
 * @receiver [ByteArray]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoEncoding 二进制编码
 * @return
 */
public fun ByteArray.encryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    cryptoEncoding: CryptoEncoding,
): ByteArray = encryptToBytes(symmetricCryptoAlgorithm, secret.toByteArray(), cryptoEncoding)

/**
 * 加密
 * @receiver [ByteArray]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoEncoding 二进制编码
 * @return
 */
public fun ByteArray.encryptToBytes(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    cryptoEncoding: CryptoEncoding,
): ByteArray = if (this.isEmpty()) {
    this
} else {
    symmetricCryptoAlgorithm.algorithm.encrypt(this, secret, cryptoEncoding)
}
