package com.tony.crypto.symmetric.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.tony.crypto.symmetric.Aes
import com.tony.crypto.symmetric.Des
import com.tony.crypto.symmetric.SymmetricCrypto
import com.tony.enums.EnumCreator
import com.tony.enums.EnumStringValue

/**
 * DigestMode is
 * @author tangli
 * @since 2023/05/26 10:45
 */

public enum class SymmetricCryptoAlgorithm(
    override val value: String,
    public val algorithm: SymmetricCrypto,
) : EnumStringValue {
    @JsonEnumDefaultValue
    AES("aes", Aes),
    DES("des", Des),
    ;

    public companion object : EnumCreator<SymmetricCryptoAlgorithm, String>(SymmetricCryptoAlgorithm::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: String): SymmetricCryptoAlgorithm? =
            super.create(value.lowercase())
    }
}

/**
 * 加解密的二进制编码
 * @property value
 */
public enum class CryptoEncoding(
    override val value: String,
) : EnumStringValue {
    @JsonEnumDefaultValue
    BASE64("base64"),
    HEX("hex"),
    ;

    public companion object : EnumCreator<CryptoEncoding, String>(CryptoEncoding::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: String): CryptoEncoding? =
            super.create(value.lowercase())
    }
}
