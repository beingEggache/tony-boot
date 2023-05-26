@file:JvmName("CipherUtils")
@file:JvmMultifileClass

package com.tony.cipher

/**
 * 加密工具类
 *
 * @author tangli
 * @since 2023/5/25 16:16
 */
import com.tony.utils.decodeBase64
import com.tony.utils.decodeHex
import com.tony.utils.encodeToBase64
import com.tony.utils.encodeToHex
import com.tony.utils.secureRandom
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

private const val DES = "DES"

@Suppress("unused")
private val INIT_PROVIDER = Security.addProvider(BouncyCastleProvider())

/**
 * des 加密字符串
 * @param secret 秘钥
 * @param mode 摘要类型 默认 [CipherDigestMode.BASE64]
 * @return
 */
@JvmOverloads
public fun String.desEncrypt(
    secret: String,
    mode: CipherDigestMode = CipherDigestMode.BASE64,
): String =
    toByteArray()
        .desEncrypt(secret.toByteArray())
        .run {
            when (mode) {
                CipherDigestMode.BASE64 -> encodeToBase64()
                CipherDigestMode.HEX -> encodeToHex()
            }
        }
        .toString(Charsets.UTF_8)

/**
 * des 加密字符串
 * @param secret 秘钥
 * @param mode 摘要类型 base64/hex
 * @return
 */
public fun String.desEncrypt(
    secret: String,
    mode: String,
): String = desEncrypt(secret, CipherDigestMode.valueOf(mode))

/**
 * des 解密字符串
 * @param secret 秘钥
 * @param mode 摘要类型 默认 [CipherDigestMode.BASE64]
 * @return
 */
public fun String.desDecrypt(
    secret: String,
    mode: CipherDigestMode = CipherDigestMode.BASE64,
): String = toByteArray()
    .run {
        when (mode) {
            CipherDigestMode.BASE64 -> decodeBase64()
            CipherDigestMode.HEX -> decodeHex()
        }
    }
    .desDecrypt(secret.toByteArray())
    .toString(Charsets.UTF_8)

/**
 * des 加密字符串
 * @param secret 秘钥
 * @param mode 摘要类型 base64/hex
 * @return
 */
public fun String.desDecrypt(
    secret: String,
    mode: String,
): String = desDecrypt(secret, CipherDigestMode.valueOf(mode))

/**
 * des 加密
 *
 * @receiver [ByteArray]
 * @param secret
 * @return
 */
public fun ByteArray.desEncrypt(secret: ByteArray): ByteArray =
    des(secret, Cipher.ENCRYPT_MODE)

/**
 * aes 解密
 *
 * @receiver [ByteArray]
 * @param secret
 * @return
 */
public fun ByteArray.desDecrypt(secret: ByteArray): ByteArray =
    des(secret, Cipher.DECRYPT_MODE)

private val desKeyFactory = SecretKeyFactory.getInstance(DES)
private fun ByteArray.des(secret: ByteArray, mode: Int): ByteArray {
    val secretKeySpec = DESKeySpec(secret)
    return Cipher.getInstance(DES)
        .apply {
            init(mode, desKeyFactory.generateSecret(secretKeySpec), secureRandom)
        }.doFinal(this)
}
