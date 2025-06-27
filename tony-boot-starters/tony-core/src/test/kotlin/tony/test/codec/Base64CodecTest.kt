package tony.test.codec

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import tony.codec.Base64Codec

/**
 * Base64Codec 编解码工具测试
 *
 * @author tangli
 * @date 2025/06/27 17:00
 */
@DisplayName("Base64Codec 编解码工具测试")
class Base64CodecTest {

    @Nested
    @DisplayName("标准Base64编码解码")
    inner class StandardBase64 {
        @Test
        @DisplayName("字符串标准Base64编码解码")
        fun testEncodeDecodeString() {
            val str = "Hello, World!"
            val encoded = Base64Codec.encodeToString(str)
            val decoded = Base64Codec.decodeToString(encoded)
            assertEquals(str, decoded)
        }

        @Test
        @DisplayName("字节数组标准Base64编码解码")
        fun testEncodeDecodeBytes() {
            val bytes = "Hello, 世界!".toByteArray(Charsets.UTF_8)
            val encoded = Base64Codec.encodeToString(bytes)
            val decoded = Base64Codec.decodeToByteArray(encoded)
            assertArrayEquals(bytes, decoded)
        }
    }

    @Nested
    @DisplayName("URL安全Base64编码解码")
    inner class UrlSafeBase64 {
        @Test
        @DisplayName("字符串URL安全Base64编码解码")
        fun testUrlSafeEncodeDecodeString() {
            val str = "Hello, 世界!http://www.baidu.com?a=1&b=2"
            val encoded = Base64Codec.encodeToStringUrlSafe(str)
            val decoded = Base64Codec.decodeToStringUrlSafe(encoded)
            assertEquals(str, decoded)
            // URL安全校验
            assertFalse(encoded.contains('+'))
            assertFalse(encoded.contains('/'))
            assertFalse(encoded.contains('='))
        }
    }

    @Nested
    @DisplayName("标准Base64与URL安全Base64对比")
    inner class StandardVsUrlSafe {
        @Test
        @DisplayName("标准Base64与URL安全Base64结果不同且均可解码")
        fun testStandardVsUrlSafe() {
            val str = "http://www.baidu.com?a=1&b=2 Hello, 世界!"
            val standard = Base64Codec.encodeToString(str)
            val urlSafe = Base64Codec.encodeToStringUrlSafe(str)
            assertNotEquals(standard, urlSafe)
            assertEquals(str, Base64Codec.decodeToString(standard))
            assertEquals(str, Base64Codec.decodeToStringUrlSafe(urlSafe))
        }
    }

    @Nested
    @DisplayName("边界与异常场景")
    inner class BoundaryCases {
        @Test
        @DisplayName("空字符串和空字节数组")
        fun testEmptyInputs() {
            val emptyStr = ""
            val emptyBytes = ByteArray(0)
            assertEquals(emptyStr, Base64Codec.decodeToString(Base64Codec.encodeToString(emptyStr)))
            assertEquals(emptyStr, Base64Codec.decodeToStringUrlSafe(Base64Codec.encodeToStringUrlSafe(emptyStr)))
            assertArrayEquals(emptyBytes, Base64Codec.decodeToByteArray(Base64Codec.encodeToString(emptyBytes)))
        }

        @Test
        @DisplayName("单字符/双字符/三字符边界")
        fun testShortStrings() {
            val one = "A"
            val two = "AB"
            val three = "ABC"
            assertEquals(one, Base64Codec.decodeToString(Base64Codec.encodeToString(one)))
            assertEquals(two, Base64Codec.decodeToString(Base64Codec.encodeToString(two)))
            assertEquals(three, Base64Codec.decodeToString(Base64Codec.encodeToString(three)))
        }
    }

    @Nested
    @DisplayName("特殊字符与多语言支持")
    inner class SpecialAndI18n {
        @Test
        @DisplayName("特殊符号编码解码")
        fun testSpecialCharacters() {
            val special = "!@#￥%……&*()_+-=[]{}|;':\",./<>?`~"
            assertEquals(special, Base64Codec.decodeToString(Base64Codec.encodeToString(special)))
            assertEquals(special, Base64Codec.decodeToStringUrlSafe(Base64Codec.encodeToStringUrlSafe(special)))
        }

        @Test
        @DisplayName("中文编码解码")
        fun testChineseCharacters() {
            val chinese = "中文测试：你好世界！"
            assertEquals(chinese, Base64Codec.decodeToString(Base64Codec.encodeToString(chinese)))
            assertEquals(chinese, Base64Codec.decodeToStringUrlSafe(Base64Codec.encodeToStringUrlSafe(chinese)))
        }
    }

    @Nested
    @DisplayName("长字符串与性能测试")
    inner class LongStringAndPerformance {
        @Test
        @DisplayName("长字符串编码解码")
        fun testLargeString() {
            val large = "A".repeat(10000)
            assertEquals(large, Base64Codec.decodeToString(Base64Codec.encodeToString(large)))
            assertEquals(large, Base64Codec.decodeToStringUrlSafe(Base64Codec.encodeToStringUrlSafe(large)))
        }

        @Test
        @DisplayName("性能简单验证")
        fun testPerformance() {
            val str = "Performance test data"
            val iterations = 1000
            val t1 = System.currentTimeMillis()
            repeat(iterations) {
                val encoded = Base64Codec.encodeToString(str)
                Base64Codec.decodeToString(encoded)
            }
            val t2 = System.currentTimeMillis()
            repeat(iterations) {
                val encoded = Base64Codec.encodeToStringUrlSafe(str)
                Base64Codec.decodeToStringUrlSafe(encoded)
            }
            val t3 = System.currentTimeMillis()
            assertTrue(t2 > t1 && t3 > t2)
        }
    }

    @Nested
    @DisplayName("参数化典型用例")
    inner class ParameterizedCases {
        @ParameterizedTest
        @ValueSource(strings = ["abc", "123", "!@#", "中文", "", "A", "AB", "ABC", "A B C", "\n\t\r"])
        @DisplayName("参数化字符串标准Base64编码解码")
        fun testParamStandardBase64(str: String) {
            assertEquals(str, Base64Codec.decodeToString(Base64Codec.encodeToString(str)))
        }
    }
}
