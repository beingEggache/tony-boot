package tony.test.crypto

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import tony.codec.enums.Encoding
import tony.crypto.symmetric.Des

/**
 * Des 对称加密工具测试
 *
 * @author tangli
 * @date 2025/06/27 17:00
 */
@DisplayName("Des 对称加密工具测试")
class DesTest {

    @Nested
    @DisplayName("基础加解密功能")
    inner class BasicEncryptDecrypt {
        @Test
        @DisplayName("DES加密解密-字节数组")
        fun testDesEncryptDecrypt() {
            val data = "Hello, DES! 你好，世界！".toByteArray(Charsets.UTF_8)
            val secret = "desSecret".toByteArray(Charsets.UTF_8)
            val encrypted = Des.encrypt(data, secret)
            val decrypted = Des.decrypt(encrypted, secret)
            assertArrayEquals(data, decrypted)
        }
    }

    @Nested
    @DisplayName("带编码加解密")
    inner class EncodingEncryptDecrypt {
        @Test
        @DisplayName("DES加密解密-Hex编码")
        fun testDesEncryptDecryptWithEncoding() {
            val data = "DES 编码测试".toByteArray(Charsets.UTF_8)
            val secret = "desSecret".toByteArray(Charsets.UTF_8)
            val encrypted = Des.encrypt(data, secret, Encoding.HEX)
            val decrypted = Des.decrypt(encrypted, secret, Encoding.HEX)
            assertArrayEquals(data, decrypted)
        }
    }

    @Nested
    @DisplayName("边界与异常场景")
    inner class BoundaryAndException {
        @Test
        @DisplayName("空数据加解密")
        fun testDesEmptyData() {
            val empty = ByteArray(0)
            val secret = "desSecret".toByteArray(Charsets.UTF_8)
            val encrypted = Des.encrypt(empty, secret)
            val decrypted = Des.decrypt(encrypted, secret)
            assertArrayEquals(empty, decrypted)
        }

        @Test
        @DisplayName("密钥长度不足抛异常")
        fun testDesKeyLength() {
            val data = "test".toByteArray()
            val shortKey = "short".toByteArray()
            assertThrows<java.security.InvalidKeyException> {
                Des.encrypt(data, shortKey)
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
            val secret = "desSecret".toByteArray(Charsets.UTF_8)
            val encrypted = Des.encrypt(data, secret)
            val decrypted = Des.decrypt(encrypted, secret)
            assertArrayEquals(data, decrypted)
        }

        @Test
        @DisplayName("长字符串加解密与性能")
        fun testLargeStringPerformance() {
            val data = "A".repeat(10000).toByteArray(Charsets.UTF_8)
            val secret = "desSecret".toByteArray(Charsets.UTF_8)
            val encrypted = Des.encrypt(data, secret)
            val decrypted = Des.decrypt(encrypted, secret)
            assertArrayEquals(data, decrypted)
            // 性能简单验证
            val iterations = 500
            val t1 = System.currentTimeMillis()
            repeat(iterations) {
                val enc = Des.encrypt(data, secret)
                Des.decrypt(enc, secret)
            }
            val t2 = System.currentTimeMillis()
            assertTrue(t2 > t1)
        }
    }
}
