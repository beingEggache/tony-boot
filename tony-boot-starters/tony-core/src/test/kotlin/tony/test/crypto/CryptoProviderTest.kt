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

package tony.test.crypto

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.slf4j.LoggerFactory
import tony.codec.enums.Encoding
import tony.crypto.CryptoProvider
import tony.crypto.symmetric.enums.SymmetricCryptoAlgorithm

/**
 * CryptoProvider 接口单元测试
 *
 * @author tangli
 * @date 2024/01/01 00:00
 * @since 1.0.0
 */
object CryptoProviderTest {

    private val logger = LoggerFactory.getLogger(CryptoProviderTest::class.java)

    /**
     * 测试 CryptoProvider 接口属性
     */
    @Test
    fun testCryptoProviderProperties() {
        logger.info("开始测试 CryptoProvider 接口属性")

        // 创建一个测试实现
        val testProvider = object : CryptoProvider {
            override val algorithm: SymmetricCryptoAlgorithm = SymmetricCryptoAlgorithm.AES
            override val secret: String = "testSecret123456789012345678901234"
            override val encoding: Encoding = Encoding.BASE64
        }

        // 测试属性
        assertNotNull(testProvider.algorithm, "算法不应为空")
        assertNotNull(testProvider.secret, "密钥不应为空")
        assertNotNull(testProvider.encoding, "编码不应为空")

        assertEquals(SymmetricCryptoAlgorithm.AES, testProvider.algorithm, "算法应该正确")
        assertEquals("testSecret123456789012345678901234", testProvider.secret, "密钥应该正确")
        assertEquals(Encoding.BASE64, testProvider.encoding, "编码应该正确")

        logger.info("CryptoProvider 接口属性测试通过")
        logger.info("算法: ${testProvider.algorithm}")
        logger.info("密钥: ${testProvider.secret}")
        logger.info("编码: ${testProvider.encoding}")
    }

    /**
     * 测试不同的算法配置
     */
    @Test
    fun testDifferentAlgorithms() {
        logger.info("开始测试不同的算法配置")

        // 测试 AES 算法
        val aesProvider = object : CryptoProvider {
            override val algorithm: SymmetricCryptoAlgorithm = SymmetricCryptoAlgorithm.AES
            override val secret: String = "aesSecret123456789012345678901234"
            override val encoding: Encoding = Encoding.BASE64
        }

        assertEquals(SymmetricCryptoAlgorithm.AES, aesProvider.algorithm, "AES 算法应该正确")
        logger.info("AES 算法配置测试通过")

        // 测试 DES 算法
        val desProvider = object : CryptoProvider {
            override val algorithm: SymmetricCryptoAlgorithm = SymmetricCryptoAlgorithm.DES
            override val secret: String = "desSecret"
            override val encoding: Encoding = Encoding.HEX
        }

        assertEquals(SymmetricCryptoAlgorithm.DES, desProvider.algorithm, "DES 算法应该正确")
        logger.info("DES 算法配置测试通过")
    }

    /**
     * 测试不同的编码配置
     */
    @Test
    fun testDifferentEncodings() {
        logger.info("开始测试不同的编码配置")

        // 测试 Base64 编码
        val base64Provider = object : CryptoProvider {
            override val algorithm: SymmetricCryptoAlgorithm = SymmetricCryptoAlgorithm.AES
            override val secret: String = "secret123456789012345678901234"
            override val encoding: Encoding = Encoding.BASE64
        }

        assertEquals(Encoding.BASE64, base64Provider.encoding, "Base64 编码应该正确")
        logger.info("Base64 编码配置测试通过")

        // 测试 Hex 编码
        val hexProvider = object : CryptoProvider {
            override val algorithm: SymmetricCryptoAlgorithm = SymmetricCryptoAlgorithm.AES
            override val secret: String = "secret123456789012345678901234"
            override val encoding: Encoding = Encoding.HEX
        }

        assertEquals(Encoding.HEX, hexProvider.encoding, "Hex 编码应该正确")
        logger.info("Hex 编码配置测试通过")
    }

    /**
     * 测试密钥长度
     */
    @Test
    fun testSecretLength() {
        logger.info("开始测试密钥长度")

        // 测试 AES 密钥长度（32字节）
        val aesProvider = object : CryptoProvider {
            override val algorithm: SymmetricCryptoAlgorithm = SymmetricCryptoAlgorithm.AES
            override val secret: String = "aesSecret12345678901234567890123"
            override val encoding: Encoding = Encoding.BASE64
        }

        assertEquals(32, aesProvider.secret.length, "AES 密钥长度应该为 32")
        logger.info("AES 密钥长度测试通过: ${aesProvider.secret.length}")

        // 测试 DES 密钥长度（8字节）
        val desProvider = object : CryptoProvider {
            override val algorithm: SymmetricCryptoAlgorithm = SymmetricCryptoAlgorithm.DES
            override val secret: String = "desSecre"
            override val encoding: Encoding = Encoding.HEX
        }

        assertEquals(8, desProvider.secret.length, "DES 密钥长度应该为 8")
        logger.info("DES 密钥长度测试通过: ${desProvider.secret.length}")
    }

    /**
     * 测试组合配置
     */
    @Test
    fun testCombinationConfigurations() {
        logger.info("开始测试组合配置")

        val configurations = listOf(
            Triple(SymmetricCryptoAlgorithm.AES, "aesSecret123456789012345678901234", Encoding.BASE64),
            Triple(SymmetricCryptoAlgorithm.AES, "aesSecret123456789012345678901234", Encoding.HEX),
            Triple(SymmetricCryptoAlgorithm.DES, "desSecret", Encoding.BASE64),
            Triple(SymmetricCryptoAlgorithm.DES, "desSecret", Encoding.HEX)
        )

        for ((algorithm, secret, encoding) in configurations) {
            val provider = object : CryptoProvider {
                override val algorithm: SymmetricCryptoAlgorithm = algorithm
                override val secret: String = secret
                override val encoding: Encoding = encoding
            }

            assertEquals(algorithm, provider.algorithm, "算法配置应该正确")
            assertEquals(secret, provider.secret, "密钥配置应该正确")
            assertEquals(encoding, provider.encoding, "编码配置应该正确")

            logger.info("组合配置测试通过: $algorithm + $encoding")
        }
    }
}
