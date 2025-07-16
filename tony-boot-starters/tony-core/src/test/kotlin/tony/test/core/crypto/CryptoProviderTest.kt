package tony.test.core.crypto

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import tony.core.codec.enums.Encoding
import tony.core.crypto.CryptoProvider
import tony.core.crypto.symmetric.enums.SymmetricCryptoAlgorithm

/**
 * CryptoProvider 接口测试
 *
 * @author tangli
 * @date 2025/06/27 17:00
 */
@DisplayName("CryptoProvider 接口测试")
class CryptoProviderTest {

    @Nested
    @DisplayName("接口属性校验")
    inner class PropertyCheck {
        @Test
        @DisplayName("基本属性不为null且正确")
        fun testProperties() {
            val provider = object : CryptoProvider {
                override val algorithm = SymmetricCryptoAlgorithm.AES
                override val secret = "testSecret123456789012345678901234"
                override val encoding = Encoding.BASE64
            }
            assertNotNull(provider.algorithm)
            assertNotNull(provider.secret)
            assertNotNull(provider.encoding)
            assertEquals(SymmetricCryptoAlgorithm.AES, provider.algorithm)
            assertEquals("testSecret123456789012345678901234", provider.secret)
            assertEquals(Encoding.BASE64, provider.encoding)
        }
    }

    @Nested
    @DisplayName("算法与编码类型")
    inner class AlgorithmAndEncoding {
        @Test
        @DisplayName("支持多种算法")
        fun testDifferentAlgorithms() {
            val aes = object : CryptoProvider {
                override val algorithm = SymmetricCryptoAlgorithm.AES
                override val secret = "aesSecret123456789012345678901234"
                override val encoding = Encoding.BASE64
            }
            val des = object : CryptoProvider {
                override val algorithm = SymmetricCryptoAlgorithm.DES
                override val secret = "desSecret"
                override val encoding = Encoding.HEX
            }
            assertEquals(SymmetricCryptoAlgorithm.AES, aes.algorithm)
            assertEquals(SymmetricCryptoAlgorithm.DES, des.algorithm)
        }

        @Test
        @DisplayName("支持多种编码")
        fun testDifferentEncodings() {
            val base64 = object : CryptoProvider {
                override val algorithm = SymmetricCryptoAlgorithm.AES
                override val secret = "secret123456789012345678901234"
                override val encoding = Encoding.BASE64
            }
            val hex = object : CryptoProvider {
                override val algorithm = SymmetricCryptoAlgorithm.AES
                override val secret = "secret123456789012345678901234"
                override val encoding = Encoding.HEX
            }
            assertEquals(Encoding.BASE64, base64.encoding)
            assertEquals(Encoding.HEX, hex.encoding)
        }
    }

    @Nested
    @DisplayName("密钥长度校验")
    inner class SecretLength {
        @Test
        @DisplayName("AES密钥长度32, DES密钥长度8")
        fun testSecretLength() {
            val aes = object : CryptoProvider {
                override val algorithm = SymmetricCryptoAlgorithm.AES
                override val secret = "a".repeat(32)
                override val encoding = Encoding.BASE64
            }
            val des = object : CryptoProvider {
                override val algorithm = SymmetricCryptoAlgorithm.DES
                override val secret = "d".repeat(8)
                override val encoding = Encoding.HEX
            }
            assertEquals(32, aes.secret.length)
            assertEquals(8, des.secret.length)
        }
    }

    @Nested
    @DisplayName("组合配置校验")
    inner class CombinationConfig {
        @Test
        @DisplayName("多种算法+密钥+编码组合")
        fun testCombinationConfigurations() {
            val configs = listOf(
                Triple(SymmetricCryptoAlgorithm.AES, "a".repeat(32), Encoding.BASE64),
                Triple(SymmetricCryptoAlgorithm.AES, "a".repeat(32), Encoding.HEX),
                Triple(SymmetricCryptoAlgorithm.DES, "d".repeat(8), Encoding.BASE64),
                Triple(SymmetricCryptoAlgorithm.DES, "d".repeat(8), Encoding.HEX)
            )
            for ((alg, sec, enc) in configs) {
                val provider = object : CryptoProvider {
                    override val algorithm = alg
                    override val secret = sec
                    override val encoding = enc
                }
                assertEquals(alg, provider.algorithm)
                assertEquals(sec, provider.secret)
                assertEquals(enc, provider.encoding)
            }
        }
    }
}
