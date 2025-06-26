package tony.test.crypto

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.slf4j.LoggerFactory
import tony.codec.enums.Encoding
import tony.crypto.symmetric.Aes

/**
 * Aes 实现单元测试
 *
 * @author tangli
 * @date 2024/01/01 00:00
 * @since 1.0.0
 */
object AesTest {
    private val logger = LoggerFactory.getLogger(AesTest::class.java)

    @Test
    fun testAesEncryptDecrypt() {
        logger.info("测试 AES 加密解密")
        val testData = "Hello, AES! 你好，世界！".toByteArray(Charsets.UTF_8)
        val secret = "aesSecret01234567890987654321012".toByteArray(Charsets.UTF_8)
        val encrypted = Aes.encrypt(testData, secret)
        val decrypted = Aes.decrypt(encrypted, secret)
        assertArrayEquals(testData, decrypted, "AES 加密解密后应还原原文")
    }

    @Test
    fun testAesEncryptDecryptWithEncoding() {
        logger.info("测试 AES 带编码加密解密")
        val testData = "AES 编码测试".toByteArray(Charsets.UTF_8)
        val secret = "aesSecret01234567890987654321012".toByteArray(Charsets.UTF_8)
        val encrypted = Aes.encrypt(testData, secret, Encoding.BASE64)
        val decrypted = Aes.decrypt(encrypted, secret, Encoding.BASE64)
        assertArrayEquals(testData, decrypted, "AES 带编码加密解密后应还原原文")
    }

    @Test
    fun testAesEmptyData() {
        logger.info("测试 AES 空数据加解密")
        val empty = ByteArray(0)
        val secret = "aesSecret01234567890987654321012".toByteArray(Charsets.UTF_8)
        val encrypted = Aes.encrypt(empty, secret)
        val decrypted = Aes.decrypt(encrypted, secret)
        assertArrayEquals(empty, decrypted, "AES 空数据加密解密应还原原文")
    }

    @Test
    fun testAesKeyLength() {
        logger.info("测试 AES 密钥长度校验")
        val testData = "test".toByteArray()
        val shortKey = "shortkey".toByteArray()
        assertThrows<IllegalArgumentException> {
            Aes.encrypt(testData, shortKey)
        }
    }
}
