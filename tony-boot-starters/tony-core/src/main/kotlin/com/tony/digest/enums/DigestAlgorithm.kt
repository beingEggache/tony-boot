package com.tony.digest.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.tony.codec.encodeToString
import com.tony.codec.enums.Encoding
import com.tony.enums.StringEnumCreator
import com.tony.enums.StringEnumValue
import java.security.MessageDigest

/**
 * DigestAlgorithm is
 * @author tangli
 * @since 2023/09/13 09:47
 */
public enum class DigestAlgorithm(
    override val value: String,
) : StringEnumValue {
    @JsonEnumDefaultValue
    MD5("md5"),
    SHA1("sha1"),
    SHA256("sha256"),
    ;

    public fun digest(src: String): String = MessageDigest
        .getInstance(this.name)
        .digest(src.toByteArray())
        .encodeToString(Encoding.HEX)

    public companion object : StringEnumCreator(DigestAlgorithm::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: String): StringEnumValue? =
            super.create(value.lowercase())
    }
}
