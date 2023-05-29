package com.tony.crypto.symmetric

import com.tony.crypto.symmetric.enums.CryptoDigestMode
import com.tony.utils.decodeBase64
import com.tony.utils.decodeHex
import com.tony.utils.encodeToBase64
import com.tony.utils.encodeToHex
import javax.crypto.Cipher

/**
 * 对称 加密/解密
 * @author tangli
 * @since 2023/05/29 09:25
 */
public sealed interface SymmetricCrypto {

    /**
     * 解密
     *
     * @param src    待解密
     * @param secret 秘钥
     * @return 解密后的返回
     */
    public fun decrypt(src: ByteArray, secret: ByteArray, mode: CryptoDigestMode): ByteArray {
        val digestedSrc = src.run {
            when (mode) {
                CryptoDigestMode.BASE64 -> decodeBase64()
                CryptoDigestMode.HEX -> decodeHex()
            }
        }
        return decrypt(digestedSrc, secret)
    }

    /**
     * 解密
     *
     * @param src    待解密
     * @param secret 秘钥
     * @return 解密后的返回
     */
    public fun decrypt(src: ByteArray, secret: ByteArray): ByteArray =
        crypto(src, secret, Cipher.DECRYPT_MODE)

    /**
     * 加密
     *
     * @param src    待加密
     * @param secret 秘钥
     * @return 加密后的返回
     */
    public fun encrypt(src: ByteArray, secret: ByteArray, mode: CryptoDigestMode): ByteArray =
        encrypt(src, secret)
            .run {
                when (mode) {
                    CryptoDigestMode.BASE64 -> encodeToBase64()
                    CryptoDigestMode.HEX -> encodeToHex()
                }
            }

    /**
     * 加密
     *
     * @param src    待加密
     * @param secret 秘钥
     * @return 加密后的返回
     */
    public fun encrypt(src: ByteArray, secret: ByteArray): ByteArray =
        crypto(src, secret, Cipher.ENCRYPT_MODE)

    /**
     * 对称加密解密
     *
     * @param src    待加密/解密
     * @param secret 秘钥
     * @param mode 加密/解密 [Cipher.ENCRYPT_MODE]/[Cipher.DECRYPT_MODE]
     * @return
     */
    public fun crypto(src: ByteArray, secret: ByteArray, mode: Int): ByteArray
}
