package com.tony.cipher

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.tony.enums.EnumCreator
import com.tony.enums.EnumStringValue

/**
 * DigestMode is
 * @author tangli
 * @since 2023/05/26 10:45
 */

/**
 * 加解密的摘要类型
 * @property value
 */
public enum class CipherDigestMode(
    override val value: String,
) : EnumStringValue {
    @JsonEnumDefaultValue
    BASE64("base64"),
    HEX("hex"),
    ;

    public companion object : EnumCreator<CipherDigestMode, String>(CipherDigestMode::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: String): CipherDigestMode? =
            super.create(value.lowercase())
    }
}
