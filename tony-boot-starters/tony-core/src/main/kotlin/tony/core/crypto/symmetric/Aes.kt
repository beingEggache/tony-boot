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

package tony.core.crypto.symmetric

import java.security.Security
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import org.bouncycastle.jce.provider.BouncyCastleProvider

/**
 * Aes对称加密单例类
 *
 * @author tangli
 * @date 2023/05/25 19:16
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

    override fun crypto(
        src: ByteArray,
        secret: ByteArray,
        mode: Int,
    ): ByteArray {
        require(secret.size == BLOCK_SIZE) { "IllegalAesKey, aesKey's length must be $BLOCK_SIZE" }
        val secretKeySpec = SecretKeySpec(secret, AES)
        return Cipher
            .getInstance(CIPHER_ALGORITHM, "BC")
            .apply {
                init(mode, secretKeySpec, IV)
            }.doFinal(src)
    }
}
