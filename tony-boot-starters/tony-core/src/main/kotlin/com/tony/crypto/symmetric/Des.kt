package com.tony.crypto.symmetric

import com.tony.utils.secureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

/**
 * Des对称加密单例类
 *
 * @author tangli
 * @since 2023/5/29 11:20
 */
public data object Des : SymmetricCrypto {

    private const val DES = "DES"

    @JvmStatic
    private val desKeyFactory = SecretKeyFactory.getInstance(DES)

    override fun crypto(src: ByteArray, secret: ByteArray, mode: Int): ByteArray {
        val secretKeySpec = DESKeySpec(secret)
        return Cipher.getInstance(DES)
            .apply {
                init(mode, desKeyFactory.generateSecret(secretKeySpec), secureRandom)
            }.doFinal(src)
    }
}
