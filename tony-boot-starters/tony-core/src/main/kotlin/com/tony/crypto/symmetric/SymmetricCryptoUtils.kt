@file:JvmName("SymmetricCryptoUtils")
@file:JvmMultifileClass

package com.tony.crypto.symmetric
/**
 * 对称加密/解密 工具
 * @author tangli
 * @since 2023/05/26 17:58
 */
import com.tony.crypto.symmetric.enums.CryptoDigestMode
import com.tony.crypto.symmetric.enums.SymmetricCryptoAlgorithm

/**
 * 解密
 * @receiver [String]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoDigestMode 摘要类型
 * @return
 */
public fun String.decryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    cryptoDigestMode: CryptoDigestMode,
): String = decryptToByte(symmetricCryptoAlgorithm, secret.toByteArray(), cryptoDigestMode)
    .toString(Charsets.UTF_8)

/**
 * 解密
 * @receiver [String]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoDigestMode 摘要类型
 * @return
 */
public fun String.decryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    cryptoDigestMode: CryptoDigestMode,
): String = decryptToByte(symmetricCryptoAlgorithm, secret, cryptoDigestMode)
    .toString(Charsets.UTF_8)

/**
 * 解密
 * @receiver [String]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoDigestMode 摘要类型
 * @return
 */
public fun String.decryptToByte(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    cryptoDigestMode: CryptoDigestMode,
): ByteArray = if (this.isBlank()) {
    ByteArray(0)
} else {
    symmetricCryptoAlgorithm.algorithm.decrypt(this.toByteArray(), secret.toByteArray(), cryptoDigestMode)
}

/**
 * 解密
 * @receiver [String]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoDigestMode 摘要类型
 * @return
 */
public fun String.decryptToByte(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    cryptoDigestMode: CryptoDigestMode,
): ByteArray = if (this.isBlank()) {
    ByteArray(0)
} else {
    symmetricCryptoAlgorithm.algorithm.decrypt(this.toByteArray(), secret, cryptoDigestMode)
}

/**
 * 解密
 * @receiver [String]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoDigestMode 摘要类型
 * @return
 */
public fun ByteArray.decryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    cryptoDigestMode: CryptoDigestMode,
): String = decryptToByte(symmetricCryptoAlgorithm, secret.toByteArray(), cryptoDigestMode)
    .toString(Charsets.UTF_8)

/**
 * 解密
 * @receiver [String]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoDigestMode 摘要类型
 * @return
 */
public fun ByteArray.decryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    cryptoDigestMode: CryptoDigestMode,
): String = decryptToByte(symmetricCryptoAlgorithm, secret, cryptoDigestMode)
    .toString(Charsets.UTF_8)

/**
 * 解密
 * @receiver [String]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoDigestMode 摘要类型
 * @return
 */
public fun ByteArray.decryptToByte(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    cryptoDigestMode: CryptoDigestMode,
): ByteArray = decryptToByte(symmetricCryptoAlgorithm, secret.toByteArray(), cryptoDigestMode)

/**
 * 解密
 * @receiver [String]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoDigestMode 摘要类型
 * @return
 */
public fun ByteArray.decryptToByte(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    cryptoDigestMode: CryptoDigestMode,
): ByteArray = if (this.isEmpty()) {
    this
} else {
    symmetricCryptoAlgorithm.algorithm.decrypt(this, secret, cryptoDigestMode)
}

/**
 * 加密
 * @receiver [String]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoDigestMode 摘要类型
 * @return
 */
public fun String.encryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    cryptoDigestMode: CryptoDigestMode,
): String = encryptToByte(symmetricCryptoAlgorithm, secret.toByteArray(), cryptoDigestMode)
    .toString(Charsets.UTF_8)

/**
 * 加密
 * @receiver [String]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoDigestMode 摘要类型
 * @return
 */
public fun String.encryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    cryptoDigestMode: CryptoDigestMode,
): String = encryptToByte(symmetricCryptoAlgorithm, secret, cryptoDigestMode)
    .toString(Charsets.UTF_8)

/**
 * 加密
 * @receiver [String]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoDigestMode 摘要类型
 * @return
 */
public fun String.encryptToByte(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    cryptoDigestMode: CryptoDigestMode,
): ByteArray = if (this.isBlank()) {
    ByteArray(0)
} else {
    symmetricCryptoAlgorithm.algorithm.encrypt(this.toByteArray(), secret.toByteArray(), cryptoDigestMode)
}

/**
 * 加密
 * @receiver [String]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoDigestMode 摘要类型
 * @return
 */
public fun String.encryptToByte(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    cryptoDigestMode: CryptoDigestMode,
): ByteArray = if (this.isBlank()) {
    ByteArray(0)
} else {
    symmetricCryptoAlgorithm.algorithm.encrypt(this.toByteArray(), secret, cryptoDigestMode)
}

/**
 * 加密
 * @receiver [String]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoDigestMode 摘要类型
 * @return
 */
public fun ByteArray.encryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    cryptoDigestMode: CryptoDigestMode,
): String = encryptToByte(symmetricCryptoAlgorithm, secret.toByteArray(), cryptoDigestMode)
    .toString(Charsets.UTF_8)

/**
 * 加密
 * @receiver [String]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoDigestMode 摘要类型
 * @return
 */
public fun ByteArray.encryptToString(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    cryptoDigestMode: CryptoDigestMode,
): String = encryptToByte(symmetricCryptoAlgorithm, secret, cryptoDigestMode)
    .toString(Charsets.UTF_8)

/**
 * 加密
 * @receiver [ByteArray]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoDigestMode 摘要类型
 * @return
 */
public fun ByteArray.encryptToByte(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: String,
    cryptoDigestMode: CryptoDigestMode,
): ByteArray = encryptToByte(symmetricCryptoAlgorithm, secret.toByteArray(), cryptoDigestMode)

/**
 * 加密
 * @receiver [ByteArray]
 * @param symmetricCryptoAlgorithm  对称加密算法
 * @param secret 秘钥
 * @param cryptoDigestMode 摘要类型
 * @return
 */
public fun ByteArray.encryptToByte(
    symmetricCryptoAlgorithm: SymmetricCryptoAlgorithm,
    secret: ByteArray,
    cryptoDigestMode: CryptoDigestMode,
): ByteArray = if (this.isEmpty()) {
    this
} else {
    symmetricCryptoAlgorithm.algorithm.encrypt(this, secret, cryptoDigestMode)
}
