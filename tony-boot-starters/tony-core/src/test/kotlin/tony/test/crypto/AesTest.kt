package tony.test.crypto

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import tony.codec.enums.Encoding
import tony.crypto.symmetric.Aes

/**
 * Aes 对称加密工具测试
 *
 * @author tangli
 * @since 1.0.0
 */
@DisplayName("Aes 对称加密工具测试")
class AesTest {

    @Nested
    @DisplayName("基础加解密功能")
    inner class BasicEncryptDecrypt {
        @Test
        @DisplayName("AES加密解密-字节数组")
        fun testAesEncryptDecrypt() {
            val data = "Hello, AES! 你好，世界！".toByteArray(Charsets.UTF_8)
            val secret = "aesSecret01234567890987654321012".toByteArray(Charsets.UTF_8)
            val encrypted = Aes.encrypt(data, secret)
            val decrypted = Aes.decrypt(encrypted, secret)
            assertArrayEquals(data, decrypted)
        }
    }

    @Nested
    @DisplayName("带编码加解密")
    inner class EncodingEncryptDecrypt {
        @Test
        @DisplayName("AES加密解密-Base64编码")
        fun testAesEncryptDecryptWithEncoding() {
            val data = "AES 编码测试".toByteArray(Charsets.UTF_8)
            val secret = "aesSecret01234567890987654321012".toByteArray(Charsets.UTF_8)
            val encrypted = Aes.encrypt(data, secret, Encoding.BASE64)
            val decrypted = Aes.decrypt(encrypted, secret, Encoding.BASE64)
            assertArrayEquals(data, decrypted)
        }
    }

    @Nested
    @DisplayName("边界与异常场景")
    inner class BoundaryAndException {
        @Test
        @DisplayName("空数据加解密")
        fun testAesEmptyData() {
            val empty = ByteArray(0)
            val secret = "aesSecret01234567890987654321012".toByteArray(Charsets.UTF_8)
            val encrypted = Aes.encrypt(empty, secret)
            val decrypted = Aes.decrypt(encrypted, secret)
            assertArrayEquals(empty, decrypted)
        }

        @Test
        @DisplayName("密钥长度不足抛异常")
        fun testAesKeyLength() {
            val data = "test".toByteArray()
            val shortKey = "shortkey".toByteArray()
            assertThrows<IllegalArgumentException> {
                Aes.encrypt(data, shortKey)
            }
        }
    }

    @Nested
    @DisplayName("特殊字符与性能")
    inner class SpecialAndPerformance {
        @Test
        @DisplayName("特殊字符加解密")
        fun testSpecialCharacters() {
            val data = "!@#￥%……&*()_+-=[]{}|;':\",./<>?`~".toByteArray(Charsets.UTF_8)
            val secret = "aesSecret01234567890987654321012".toByteArray(Charsets.UTF_8)
            val encrypted = Aes.encrypt(data, secret)
            val decrypted = Aes.decrypt(encrypted, secret)
            assertArrayEquals(data, decrypted)
        }

        @Test
        @DisplayName("长字符串加解密与性能")
        fun testLargeStringPerformance() {
            val data = "A".repeat(10000).toByteArray(Charsets.UTF_8)
            val secret = "aesSecret01234567890987654321012".toByteArray(Charsets.UTF_8)
            val encrypted = Aes.encrypt(data, secret)
            val decrypted = Aes.decrypt(encrypted, secret)
            assertArrayEquals(data, decrypted)
            // 性能简单验证
            val iterations = 500
            val t1 = System.currentTimeMillis()
            repeat(iterations) {
                val enc = Aes.encrypt(data, secret)
                Aes.decrypt(enc, secret)
            }
            val t2 = System.currentTimeMillis()
            assertTrue(t2 > t1)
        }
    }
} 