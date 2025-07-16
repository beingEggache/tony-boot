package tony.test.core.crypto

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import tony.core.codec.enums.Encoding
import tony.core.crypto.symmetric.Aes
import tony.core.crypto.symmetric.Des
import tony.core.crypto.symmetric.SymmetricCrypto

/**
 * SymmetricCrypto 接口多态与实现测试
 *
 * @author tangli
 * @date 2025/06/27 17:00
 */
@DisplayName("SymmetricCrypto 接口多态与实现测试")
class SymmetricCryptoTest {

    @Nested
    @DisplayName("多态加解密功能")
    inner class PolymorphicEncryptDecrypt {
        @Test
        @DisplayName("AES/Des 多态加解密")
        fun testPolymorphicEncryptDecrypt() {
            val data = "Hello, 多态! 你好，世界！".toByteArray(Charsets.UTF_8)
            val aesKey = "aesSecret01234567890987654321012".toByteArray(Charsets.UTF_8)
            val desKey = "desSecret".toByteArray(Charsets.UTF_8)
            val impls: List<Pair<SymmetricCrypto, ByteArray>> = listOf(
                Aes to aesKey,
                Des to desKey
            )
            for ((crypto, key) in impls) {
                val encrypted = crypto.encrypt(data, key)
                val decrypted = crypto.decrypt(encrypted, key)
                assertArrayEquals(data, decrypted)
            }
        }
    }

    @Nested
    @DisplayName("编码参数支持")
    inner class EncodingSupport {
        @Test
        @DisplayName("AES/Des 支持Base64/Hex编码")
        fun testEncodingParam() {
            val data = "编码参数测试".toByteArray(Charsets.UTF_8)
            val aesKey = "aesSecret01234567890987654321012".toByteArray(Charsets.UTF_8)
            val desKey = "desSecret".toByteArray(Charsets.UTF_8)
            val impls: List<Pair<SymmetricCrypto, ByteArray>> = listOf(
                Aes to aesKey,
                Des to desKey
            )
            val encodings = listOf(Encoding.BASE64, Encoding.HEX)
            for ((crypto, key) in impls) {
                for (enc in encodings) {
                    val encrypted = crypto.encrypt(data, key, enc)
                    val decrypted = crypto.decrypt(encrypted, key, enc)
                    assertArrayEquals(data, decrypted)
                }
            }
        }
    }

    @Nested
    @DisplayName("边界与异常场景")
    inner class BoundaryAndException {
        @Test
        @DisplayName("空数据加解密")
        fun testEmptyData() {
            val empty = ByteArray(0)
            val aesKey = "aesSecret01234567890987654321012".toByteArray(Charsets.UTF_8)
            val desKey = "desSecret".toByteArray(Charsets.UTF_8)
            val impls: List<Pair<SymmetricCrypto, ByteArray>> = listOf(
                Aes to aesKey,
                Des to desKey
            )
            for ((crypto, key) in impls) {
                val encrypted = crypto.encrypt(empty, key)
                val decrypted = crypto.decrypt(encrypted, key)
                assertArrayEquals(empty, decrypted)
            }
        }

        @Test
        @DisplayName("密钥长度不足抛异常")
        fun testKeyLengthException() {
            val data = "test".toByteArray()
            val shortAesKey = "shortkey".toByteArray()
            val shortDesKey = "short".toByteArray()
            assertThrows<IllegalArgumentException> { Aes.encrypt(data, shortAesKey) }
            assertThrows<java.security.InvalidKeyException> { Des.encrypt(data, shortDesKey) }
        }
    }

    @Nested
    @DisplayName("特殊字符与多语言")
    inner class SpecialAndI18n {
        @Test
        @DisplayName("特殊字符加解密")
        fun testSpecialCharacters() {
            val data = "!@#￥%……&*()_+-=[]{}|;':\",./<>?`~".toByteArray(Charsets.UTF_8)
            val aesKey = "aesSecret01234567890987654321012".toByteArray(Charsets.UTF_8)
            val desKey = "desSecret".toByteArray(Charsets.UTF_8)
            val impls: List<Pair<SymmetricCrypto, ByteArray>> = listOf(
                Aes to aesKey,
                Des to desKey
            )
            for ((crypto, key) in impls) {
                val encrypted = crypto.encrypt(data, key)
                val decrypted = crypto.decrypt(encrypted, key)
                assertArrayEquals(data, decrypted)
            }
        }

        @Test
        @DisplayName("中文加解密")
        fun testChineseCharacters() {
            val data = "对称加密中文测试：你好世界！".toByteArray(Charsets.UTF_8)
            val aesKey = "aesSecret01234567890987654321012".toByteArray(Charsets.UTF_8)
            val desKey = "desSecret".toByteArray(Charsets.UTF_8)
            val impls: List<Pair<SymmetricCrypto, ByteArray>> = listOf(
                Aes to aesKey,
                Des to desKey
            )
            for ((crypto, key) in impls) {
                val encrypted = crypto.encrypt(data, key)
                val decrypted = crypto.decrypt(encrypted, key)
                assertArrayEquals(data, decrypted)
            }
        }
    }

    @Nested
    @DisplayName("长字符串与性能")
    inner class LongStringAndPerformance {
        @Test
        @DisplayName("长字符串加解密与性能")
        fun testLargeStringPerformance() {
            val data = "A".repeat(10000).toByteArray(Charsets.UTF_8)
            val aesKey = "aesSecret01234567890987654321012".toByteArray(Charsets.UTF_8)
            val desKey = "desSecret".toByteArray(Charsets.UTF_8)
            val impls: List<Pair<SymmetricCrypto, ByteArray>> = listOf(
                Aes to aesKey,
                Des to desKey
            )
            for ((crypto, key) in impls) {
                val encrypted = crypto.encrypt(data, key)
                val decrypted = crypto.decrypt(encrypted, key)
                assertArrayEquals(data, decrypted)
                // 性能简单验证
                val iterations = 200
                val t1 = System.currentTimeMillis()
                repeat(iterations) {
                    val enc = crypto.encrypt(data, key)
                    crypto.decrypt(enc, key)
                }
                val t2 = System.currentTimeMillis()
                assertTrue(t2 > t1)
            }
        }
    }

    @Nested
    @DisplayName("参数化典型用例")
    inner class ParameterizedCases {
        @ParameterizedTest
        @ValueSource(strings = ["abc", "123", "!@#", "中文", "", "A", "AB", "ABC", "A B C", "\n\t\r"])
        @DisplayName("参数化字符串多态加解密")
        fun testParamPolymorphicEncryptDecrypt(str: String) {
            val data = str.toByteArray(Charsets.UTF_8)
            val aesKey = "aesSecret01234567890987654321012".toByteArray(Charsets.UTF_8)
            val desKey = "desSecret".toByteArray(Charsets.UTF_8)
            val impls: List<Pair<SymmetricCrypto, ByteArray>> = listOf(
                Aes to aesKey,
                Des to desKey
            )
            for ((crypto, key) in impls) {
                val encrypted = crypto.encrypt(data, key)
                val decrypted = crypto.decrypt(encrypted, key)
                assertArrayEquals(data, decrypted)
            }
        }
    }
}
