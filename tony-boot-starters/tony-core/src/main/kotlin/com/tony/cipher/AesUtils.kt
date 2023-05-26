@file:JvmName("CipherUtils")
@file:JvmMultifileClass

package com.tony.cipher

import com.tony.utils.decodeBase64
import com.tony.utils.decodeHex
import com.tony.utils.encodeToBase64
import com.tony.utils.encodeToHex
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * 加密工具类
 *
 * @author tangli
 * @since 2023/5/25 16:16
 */
/**
 * 块长度
 */
internal const val BLOCK_SIZE = 32

private const val CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding"

private const val AES = "AES"

private val IV = IvParameterSpec("0000000000000000".toByteArray())

@Suppress("unused")
private val INIT_PROVIDER = Security.addProvider(BouncyCastleProvider())

/**
 * aes 加密字符串
 * @param secret 秘钥
 * @param mode 摘要类型 默认 [CipherDigestMode.BASE64]
 * @return
 */
@JvmOverloads
public fun String.aesEncrypt(
    secret: String,
    mode: CipherDigestMode = CipherDigestMode.BASE64,
): String =
    toByteArray()
        .aesEncrypt(secret.toByteArray())
        .run {
            when (mode) {
                CipherDigestMode.BASE64 -> encodeToBase64()
                CipherDigestMode.HEX -> encodeToHex()
            }
        }
        .toString(Charsets.UTF_8)

/**
 * aes 加密字符串
 * @param secret 秘钥
 * @param mode 摘要类型 base64/hex
 * @return
 */
public fun String.aesEncrypt(
    secret: String,
    mode: String,
): String = aesEncrypt(secret, CipherDigestMode.valueOf(mode))

/**
 * aes 解密字符串
 * @param secret 秘钥
 * @param mode 摘要类型 默认 [CipherDigestMode.BASE64]
 * @return
 */
public fun String.aesDecrypt(
    secret: String,
    mode: CipherDigestMode = CipherDigestMode.BASE64,
): String = toByteArray()
    .run {
        when (mode) {
            CipherDigestMode.BASE64 -> decodeBase64()
            CipherDigestMode.HEX -> decodeHex()
        }
    }
    .aesDecrypt(secret.toByteArray())
    .toString(Charsets.UTF_8)

/**
 * aes 加密字符串
 * @param secret 秘钥
 * @param mode 摘要类型 base64/hex
 * @return
 */
public fun String.aesDecrypt(
    secret: String,
    mode: String,
): String = aesDecrypt(secret, CipherDigestMode.valueOf(mode))

/**
 * aes 加密
 *
 * @receiver [ByteArray]
 * @param secret
 * @return
 */
public fun ByteArray.aesEncrypt(secret: ByteArray): ByteArray =
    aes(secret, Cipher.ENCRYPT_MODE)

/**
 * aes 解密
 *
 * @receiver [ByteArray]
 * @param secret
 * @return
 */
public fun ByteArray.aesDecrypt(secret: ByteArray): ByteArray =
    aes(secret, Cipher.DECRYPT_MODE)

private fun ByteArray.aes(secret: ByteArray, mode: Int): ByteArray {
    if (secret.size != BLOCK_SIZE) {
        throw IllegalArgumentException("IllegalAesKey, aesKey's length must be $BLOCK_SIZE")
    }
    val secretKeySpec = SecretKeySpec(secret, AES)
    return Cipher.getInstance(CIPHER_ALGORITHM, "BC")
        .apply {
            init(mode, secretKeySpec, IV)
        }.doFinal(this)
}
