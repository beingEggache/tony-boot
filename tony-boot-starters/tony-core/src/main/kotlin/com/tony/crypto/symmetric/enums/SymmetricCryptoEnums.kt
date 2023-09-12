package com.tony.crypto.symmetric.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.tony.crypto.symmetric.Aes
import com.tony.crypto.symmetric.Des
import com.tony.crypto.symmetric.SymmetricCrypto
import com.tony.enums.StringEnumCreator
import com.tony.enums.StringEnumValue


/**
 * 对称加密算法
 * @author Tang Li
 * @date 2023/09/12 17:46
 * @since 1.0.0
 */
public enum class SymmetricCryptoAlgorithm(
    override val value: String,
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
