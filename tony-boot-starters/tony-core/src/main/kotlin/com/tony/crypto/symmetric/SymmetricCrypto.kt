package com.tony.crypto.symmetric

import com.tony.codec.enums.Encoding
import javax.crypto.Cipher

/**
 * 对称 加密/解密
 * @author Tang Li
 * @date 2023/05/29 09:25
 */
public sealed interface SymmetricCrypto {

    /**
     * 解密
     *
     * @param src    待解密
     * @param secret 秘钥
     * @return 解密后的返回
     */
    public fun decrypt(src: ByteArray, secret: ByteArray, encoding: Encoding): ByteArray {
        val digestedSrc = encoding.codec.encodeToByteArray(src)
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
    public fun encrypt(src: ByteArray, secret: ByteArray, encoding: Encoding): ByteArray =
        encrypt(src, secret)
            .run {
                encoding.codec.encodeToByteArray(this)
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
