package com.tony.codec.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.tony.codec.Base64Codec
import com.tony.codec.Codec
import com.tony.codec.HexCodec
import com.tony.enums.StringEnumCreator
import com.tony.enums.StringEnumValue


/**
 * 编码
 * @author Tang Li
 * @date 2023/09/12 17:45
 * @since 1.0.0
 */
public enum class Encoding(
    override val value: String,
    public val codec: Codec,
) : StringEnumValue {
    @JsonEnumDefaultValue
    BASE64("base64", Base64Codec),
    HEX("hex", HexCodec),
    ;

    public companion object : StringEnumCreator(Encoding::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: String): StringEnumValue? =
            super.create(value.lowercase())
    }
}
