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

@file:JvmName("Digests")

package tony.utils

/**
 * 摘要工具类
 *
 * @author tangli
 * @date 2022/09/29 19:20
 */
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import java.security.MessageDigest
import tony.codec.encodeToString
import tony.codec.enums.Encoding
import tony.enums.StringEnumCreator
import tony.enums.StringEnumValue

/**
 * 字符串转为MD5
 * @return [String]
 * @author tangli
 * @date 2023/09/27 19:21
 */
public fun CharSequence.md5(): String =
    DigestAlgorithm.MD5.digest(this)

/**
 * 字符串转为 sha1
 * @return [String]
 * @author tangli
 * @date 2023/09/27 19:21
 */
public fun CharSequence.sha1(): String =
    DigestAlgorithm.SHA1.digest(this)

/**
 * 字符串转为 sha256
 * @return [String]
 * @author tangli
 * @date 2023/09/27 19:21
 */
public fun CharSequence.sha256(): String =
    DigestAlgorithm.SHA256.digest(this)

/**
 * 摘要算法
 * @author tangli
 * @date 2023/09/13 19:47
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
     * @author tangli
     * @date 2023/09/27 19:21
     * @since 1.0.0
     */
    public fun digest(src: CharSequence): String =
        MessageDigest
            .getInstance(this.name)
            .digest(src.toString().toByteArray(Charsets.UTF_8))
            .encodeToString(Encoding.HEX)

    public companion object : StringEnumCreator(DigestAlgorithm::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: String): StringEnumValue? =
            super.create(value.lowercase())
    }
}
