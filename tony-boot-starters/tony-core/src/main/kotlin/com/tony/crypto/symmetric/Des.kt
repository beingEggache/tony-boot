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

package com.tony.crypto.symmetric

import com.tony.utils.secureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

/**
 * Des对称加密单例类
 *
 * @author Tang Li
 * @date 2023/5/29 11:20
 */
public data object Des : SymmetricCrypto {
    private const val DES = "DES"

    @JvmStatic
    private val desKeyFactory = SecretKeyFactory.getInstance(DES)

    override fun crypto(
        src: ByteArray,
        secret: ByteArray,
        mode: Int,
    ): ByteArray {
        val secretKeySpec = DESKeySpec(secret)
        return Cipher.getInstance(DES)
            .apply {
                init(mode, desKeyFactory.generateSecret(secretKeySpec), secureRandom)
            }.doFinal(src)
    }
}
