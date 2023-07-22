package com.tony.crypto.symmetric

import java.security.Security
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import org.bouncycastle.jce.provider.BouncyCastleProvider

/**
 * Aes对称加密单例类
 *
 * @author tangli
 * @since 2023/5/25 16:16
 */
public data object Aes : SymmetricCrypto {

    /**
     * 块长度
     */
    private const val BLOCK_SIZE = 32

    private const val CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding"

    private const val AES = "AES"

    @JvmStatic
    private val IV = IvParameterSpec("0000000000000000".toByteArray())

    init {
        Security.addProvider(BouncyCastleProvider())
    }

    override fun crypto(src: ByteArray, secret: ByteArray, mode: Int): ByteArray {
        require(secret.size == BLOCK_SIZE) { "IllegalAesKey, aesKey's length must be $BLOCK_SIZE" }
        val secretKeySpec = SecretKeySpec(secret, AES)
        return Cipher.getInstance(CIPHER_ALGORITHM, "BC")
            .apply {
                init(mode, secretKeySpec, IV)
            }.doFinal(src)
    }
}
