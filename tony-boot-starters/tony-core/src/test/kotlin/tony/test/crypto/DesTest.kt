package tony.test.crypto

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.assertNotNull
import org.slf4j.LoggerFactory
import tony.codec.enums.Encoding
import tony.crypto.symmetric.Des

/**
 * Des 实现单元测试
 *
 * @author tangli
 * @date 2024/01/01 00:00
 * @since 1.0.0
 */
object DesTest {
    private val logger = LoggerFactory.getLogger(DesTest::class.java)

    @Test
    fun testDesEncryptDecrypt() {
        logger.info("测试 DES 加密解密")
        val testData = "Hello, DES! 你好，世界！".toByteArray(Charsets.UTF_8)
        val secret = "desSecret".toByteArray(Charsets.UTF_8)
        val encrypted = Des.encrypt(testData, secret)
        val decrypted = Des.decrypt(encrypted, secret)
        assertArrayEquals(testData, decrypted, "DES 加密解密后应还原原文")
    }

    @Test
    fun testDesEncryptDecryptWithEncoding() {
        logger.info("测试 DES 带编码加密解密")
        val testData = "DES 编码测试".toByteArray(Charsets.UTF_8)
        val secret = "desSecret".toByteArray(Charsets.UTF_8)
        val encrypted = Des.encrypt(testData, secret, Encoding.HEX)
        val decrypted = Des.decrypt(encrypted, secret, Encoding.HEX)
        assertArrayEquals(testData, decrypted, "DES 带编码加密解密后应还原原文")
    }

    @Test
    fun testDesEmptyData() {
        logger.info("测试 DES 空数据加解密")
        val empty = ByteArray(0)
        val secret = "desSecret".toByteArray(Charsets.UTF_8)
        val encrypted = Des.encrypt(empty, secret)
        val decrypted = Des.decrypt(encrypted, secret)
        assertArrayEquals(empty, decrypted, "DES 空数据加密解密应还原原文")
    }

    @Test
    fun testDesKeyLength() {
        logger.info("测试 DES 密钥长度校验")
        val testData = "test".toByteArray()
        val shortKey = "short".toByteArray()
        assertThrows<java.security.InvalidKeyException> {
            Des.encrypt(testData, shortKey)
        }
    }
} 