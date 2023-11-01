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

package com.tony.digest.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.tony.codec.encodeToString
import com.tony.codec.enums.Encoding
import com.tony.enums.StringEnumCreator
import com.tony.enums.StringEnumValue
import java.security.MessageDigest

/**
 * 摘要算法
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

    /**
     * 生成摘要
     * @param [src] src
     * @return [String]
     * @author Tang Li
     * @date 2023/09/27 18:21
     * @since 1.0.0
     */
    public fun digest(src: String): String =
        MessageDigest
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
