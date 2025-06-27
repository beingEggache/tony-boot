package tony.test.crypto

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import tony.codec.enums.Encoding
import tony.crypto.symmetric.enums.SymmetricCryptoAlgorithm
import tony.crypto.symmetric.decryptToBytes
import tony.crypto.symmetric.decryptToString
import tony.crypto.symmetric.encryptToBytes
import tony.crypto.symmetric.encryptToString

/**
 * SymmetricCryptos 扩展方法工具测试
 *
 * @author tangli
 * @since 1.0.0
 */
@DisplayName("SymmetricCryptos 扩展方法工具测试")
class SymmetricCryptosTest {

    private val aesAlg = SymmetricCryptoAlgorithm.AES
    private val desAlg = SymmetricCryptoAlgorithm.DES
    private val aesKeyStr = "aesSecret01234567890987654321012"
    private val desKeyStr = "desSecret"
    private val aesKey = aesKeyStr.toByteArray(Charsets.UTF_8)
    private val desKey = desKeyStr.toByteArray(Charsets.UTF_8)
    private val encodings = listOf(Encoding.BASE64, Encoding.HEX)

    @Nested
    @DisplayName("CharSequence 扩展加解密")
    inner class CharSequenceExtension {
        @Test
        @DisplayName("字符串加密解密-支持多算法多编码")
        fun testEncryptDecryptString() {
            val str = "Hello, Symmetric! 你好，世界！"
            for (alg in listOf(aesAlg, desAlg)) {
                for (enc in encodings) {
                    val encrypted = str.encryptToString(alg, if (alg==aesAlg) aesKeyStr else desKeyStr, enc)
                    val decrypted = encrypted.decryptToString(alg, if (alg==aesAlg) aesKeyStr else desKeyStr, enc)
                    assertEquals(str, decrypted)
                }
            }
        }
    }

    @Nested
    @DisplayName("ByteArray 扩展加解密")
    inner class ByteArrayExtension {
        @Test
        @DisplayName("字节数组加密解密-支持多算法多编码")
        fun testEncryptDecryptBytes() {
            val bytes = "Hello, Symmetric!".toByteArray(Charsets.UTF_8)
            for (alg in listOf(aesAlg, desAlg)) {
                for (enc in encodings) {
                    val encrypted = bytes.encryptToBytes(alg, if (alg==aesAlg) aesKey else desKey, enc)
                    val decrypted = encrypted.decryptToBytes(alg, if (alg==aesAlg) aesKey else desKey, enc)
                    assertArrayEquals(bytes, decrypted)
                }
            }
        }
    }

    @Nested
    @DisplayName("空输入与边界场景")
    inner class EmptyAndBoundary {
        @Test
        @DisplayName("空字符串和空字节数组加解密")
        fun testEmptyInputs() {
            val emptyStr = ""
            val emptyBytes = ByteArray(0)
            for (alg in listOf(aesAlg, desAlg)) {
                for (enc in encodings) {
                    val encryptedStr = emptyStr.encryptToString(alg, if (alg==aesAlg) aesKeyStr else desKeyStr, enc)
                    val decryptedStr = encryptedStr.decryptToString(alg, if (alg==aesAlg) aesKeyStr else desKeyStr, enc)
                    assertEquals(emptyStr, decryptedStr)
                    val encryptedBytes = emptyBytes.encryptToBytes(alg, if (alg==aesAlg) aesKey else desKey, enc)
                    val decryptedBytes = encryptedBytes.decryptToBytes(alg, if (alg==aesAlg) aesKey else desKey, enc)
                    assertArrayEquals(emptyBytes, decryptedBytes)
                }
            }
        }
    }

    @Nested
    @DisplayName("特殊字符与多语言")
    inner class SpecialAndI18n {
        @Test
        @DisplayName("特殊字符加解密")
        fun testSpecialCharacters() {
            val special = "!@#￥%……&*()_+-=[]{}|;':\",./<>?`~"
            for (alg in listOf(aesAlg, desAlg)) {
                for (enc in encodings) {
                    val encrypted = special.encryptToString(alg, if (alg==aesAlg) aesKeyStr else desKeyStr, enc)
                    val decrypted = encrypted.decryptToString(alg, if (alg==aesAlg) aesKeyStr else desKeyStr, enc)
                    assertEquals(special, decrypted)
                }
            }
        }

        @Test
        @DisplayName("中文加解密")
        fun testChineseCharacters() {
            val chinese = "对称加密中文测试：你好世界！"
            for (alg in listOf(aesAlg, desAlg)) {
                for (enc in encodings) {
                    val encrypted = chinese.encryptToString(alg, if (alg==aesAlg) aesKeyStr else desKeyStr, enc)
                    val decrypted = encrypted.decryptToString(alg, if (alg==aesAlg) aesKeyStr else desKeyStr, enc)
                    assertEquals(chinese, decrypted)
                }
            }
        }
    }

    @Nested
    @DisplayName("长字符串与性能")
    inner class LongStringAndPerformance {
        @Test
        @DisplayName("长字符串加解密与性能")
        fun testLargeStringPerformance() {
            val large = "A".repeat(10000)
            for (alg in listOf(aesAlg, desAlg)) {
                for (enc in encodings) {
                    val encrypted = large.encryptToString(alg, if (alg==aesAlg) aesKeyStr else desKeyStr, enc)
                    val decrypted = encrypted.decryptToString(alg, if (alg==aesAlg) aesKeyStr else desKeyStr, enc)
                    assertEquals(large, decrypted)
                    // 性能简单验证
                    val iterations = 200
                    val t1 = System.currentTimeMillis()
                    repeat(iterations) {
                        val encStr = large.encryptToString(alg, if (alg==aesAlg) aesKeyStr else desKeyStr, enc)
                        encStr.decryptToString(alg, if (alg==aesAlg) aesKeyStr else desKeyStr, enc)
                    }
                    val t2 = System.currentTimeMillis()
                    assertTrue(t2 > t1)
                }
            }
        }
    }

    @Nested
    @DisplayName("参数化典型用例")
    inner class ParameterizedCases {
        @ParameterizedTest
        @ValueSource(strings = ["abc", "123", "!@#", "中文", "", "A", "AB", "ABC", "A B C"])
        @DisplayName("参数化字符串扩展加解密")
        fun testParamEncryptDecrypt(str: String) {
            for (alg in listOf(aesAlg, desAlg)) {
                for (enc in encodings) {
                    val encrypted = str.encryptToString(alg, if (alg==aesAlg) aesKeyStr else desKeyStr, enc)
                    val decrypted = encrypted.decryptToString(alg, if (alg==aesAlg) aesKeyStr else desKeyStr, enc)
                    assertEquals(str, decrypted)
                }
            }
        }
    }
}
