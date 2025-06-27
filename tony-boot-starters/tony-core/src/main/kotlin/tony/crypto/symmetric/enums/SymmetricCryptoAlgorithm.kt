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

package tony.crypto.symmetric.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import tony.crypto.symmetric.Aes
import tony.crypto.symmetric.Des
import tony.crypto.symmetric.SymmetricCrypto
import tony.enums.StringEnumCreator
import tony.enums.StringEnumValue

/**
 * 对称加密算法
 * @author tangli
 * @date 2023/09/12 19:46
 */
public enum class SymmetricCryptoAlgorithm(
    override val value: String,
    @JvmField
    public val algorithm: SymmetricCrypto,
) : StringEnumValue {
    @JsonEnumDefaultValue
    AES("aes", Aes),
    DES("des", Des),
    ;

    public companion object : StringEnumCreator(SymmetricCryptoAlgorithm::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: String): StringEnumValue? =
            super.create(value.lowercase())
    }
}
