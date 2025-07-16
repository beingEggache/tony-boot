package tony.test.core.codec

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import tony.core.codec.decodeToByteArray
import tony.core.codec.decodeToString
import tony.core.codec.encodeToByteArray
import tony.core.codec.encodeToString
import tony.core.codec.enums.Encoding

/**
 * Codecs 编解码扩展方法测试
 *
 * @author tangli
 * @date 2025/06/27 17:00
 */
@DisplayName("Codecs 编解码扩展方法测试")
class CodecsTest {

    @Nested
    @DisplayName("CharSequence 编解码扩展")
    inner class CharSequenceCodec {
        @Test
        @DisplayName("Base64/Hex 编解码字符串")
        fun testCharSequenceEncodeDecode() {
            val str = "Hello, World! 你好，世界！"
            val base64 = str.encodeToString(Encoding.BASE64)
            val hex = str.encodeToString(Encoding.HEX)
            assertEquals(str, base64.decodeToString(Encoding.BASE64))
            assertEquals(str, hex.decodeToString(Encoding.HEX))
        }

        @Test
        @DisplayName("字符串编码为字节数组")
        fun testEncodeToByteArray() {
            val str = "Hello, World!"
            val base64Bytes = str.encodeToByteArray(Encoding.BASE64)
            val hexBytes = str.encodeToByteArray(Encoding.HEX)
            assertTrue(base64Bytes.isNotEmpty())
            assertTrue(hexBytes.isNotEmpty())
        }
    }

    @Nested
    @DisplayName("CharSequence 解码扩展")
    inner class CharSequenceDecode {
        @Test
        @DisplayName("Base64/Hex 字符串解码为原文")
        fun testDecodeToString() {
            val str = "测试数据Test Data"
            val base64 = str.encodeToString(Encoding.BASE64)
            val hex = str.encodeToString(Encoding.HEX)
            assertEquals(str, base64.decodeToString(Encoding.BASE64))
            assertEquals(str, hex.decodeToString(Encoding.HEX))
        }

        @Test
        @DisplayName("Base64/Hex 字符串解码为字节数组")
        fun testDecodeToByteArray() {
            val str = "测试数据Test Data"
            val base64 = str.encodeToString(Encoding.BASE64)
            val hex = str.encodeToString(Encoding.HEX)
            assertArrayEquals(str.toByteArray(Charsets.UTF_8), base64.decodeToByteArray(Encoding.BASE64))
            assertArrayEquals(str.toByteArray(Charsets.UTF_8), hex.decodeToByteArray(Encoding.HEX))
        }
    }

    @Nested
    @DisplayName("ByteArray 编解码扩展")
    inner class ByteArrayCodec {
        @Test
        @DisplayName("Base64/Hex 字节数组编解码")
        fun testByteArrayEncodeDecode() {
            val bytes = "Hello, 世界!".toByteArray(Charsets.UTF_8)
            val base64 = bytes.encodeToString(Encoding.BASE64)
            val hex = bytes.encodeToString(Encoding.HEX)
            assertEquals(String(bytes, Charsets.UTF_8), base64.decodeToString(Encoding.BASE64))
            assertEquals(String(bytes, Charsets.UTF_8), hex.decodeToString(Encoding.HEX))
        }

        @Test
        @DisplayName("字节数组编码为字节数组")
        fun testEncodeToByteArray() {
            val bytes = "Hello, World!".toByteArray(Charsets.UTF_8)
            val base64Bytes = bytes.encodeToByteArray(Encoding.BASE64)
            val hexBytes = bytes.encodeToByteArray(Encoding.HEX)
            assertTrue(base64Bytes.isNotEmpty())
            assertTrue(hexBytes.isNotEmpty())
        }
    }

    @Nested
    @DisplayName("ByteArray 解码扩展")
    inner class ByteArrayDecode {
        @Test
        @DisplayName("Base64/Hex 字节数组解码为原文")
        fun testDecodeToString() {
            val str = "Test Data"
            val bytes = str.toByteArray(Charsets.UTF_8)
            val base64 = bytes.encodeToString(Encoding.BASE64)
            val hex = bytes.encodeToString(Encoding.HEX)
            assertEquals(str, base64.toByteArray(Charsets.UTF_8).decodeToString(Encoding.BASE64))
            assertEquals(str, hex.toByteArray(Charsets.UTF_8).decodeToString(Encoding.HEX))
        }

        @Test
        @DisplayName("Base64/Hex 字节数组解码为字节数组")
        fun testDecodeToByteArray() {
            val str = "Test Data"
            val bytes = str.toByteArray(Charsets.UTF_8)
            val base64 = bytes.encodeToString(Encoding.BASE64)
            val hex = bytes.encodeToString(Encoding.HEX)
            assertArrayEquals(bytes, base64.toByteArray(Charsets.UTF_8).decodeToByteArray(Encoding.BASE64))
            assertArrayEquals(bytes, hex.toByteArray(Charsets.UTF_8).decodeToByteArray(Encoding.HEX))
        }
    }

    @Nested
    @DisplayName("空输入与边界场景")
    inner class EmptyAndBoundary {
        @Test
        @DisplayName("空字符串和空字节数组")
        fun testEmptyInputs() {
            val emptyStr = ""
            val emptyBytes = ByteArray(0)
            assertEquals(emptyStr, emptyStr.encodeToString(Encoding.BASE64).decodeToString(Encoding.BASE64))
            assertEquals(emptyStr, emptyStr.encodeToString(Encoding.HEX).decodeToString(Encoding.HEX))
            assertArrayEquals(emptyBytes, emptyBytes.encodeToString(Encoding.BASE64).decodeToByteArray(Encoding.BASE64))
            assertArrayEquals(emptyBytes, emptyBytes.encodeToString(Encoding.HEX).decodeToByteArray(Encoding.HEX))
        }

        @Test
        @DisplayName("单字符/双字符边界")
        fun testShortStrings() {
            val one = "A"
            val two = "AB"
            assertEquals(one, one.encodeToString(Encoding.BASE64).decodeToString(Encoding.BASE64))
            assertEquals(two, two.encodeToString(Encoding.HEX).decodeToString(Encoding.HEX))
        }
    }

    @Nested
    @DisplayName("特殊字符与多语言支持")
    inner class SpecialAndI18n {
        @Test
        @DisplayName("特殊符号编码解码")
        fun testSpecialCharacters() {
            val special = "!@#￥%……&*()_+-=[]{}|;':\",./<>?`~"
            assertEquals(special, special.encodeToString(Encoding.BASE64).decodeToString(Encoding.BASE64))
            assertEquals(special, special.encodeToString(Encoding.HEX).decodeToString(Encoding.HEX))
        }

        @Test
        @DisplayName("中文编码解码")
        fun testChineseCharacters() {
            val chinese = "中文测试：你好世界！"
            assertEquals(chinese, chinese.encodeToString(Encoding.BASE64).decodeToString(Encoding.BASE64))
            assertEquals(chinese, chinese.encodeToString(Encoding.HEX).decodeToString(Encoding.HEX))
        }
    }

    @Nested
    @DisplayName("长字符串与性能测试")
    inner class LongStringAndPerformance {
        @Test
        @DisplayName("长字符串编码解码")
        fun testLargeString() {
            val large = "A".repeat(10000)
            assertEquals(large, large.encodeToString(Encoding.BASE64).decodeToString(Encoding.BASE64))
            assertEquals(large, large.encodeToString(Encoding.HEX).decodeToString(Encoding.HEX))
        }

        @Test
        @DisplayName("性能简单验证")
        fun testPerformance() {
            val str = "Performance test data"
            val iterations = 1000
            val t1 = System.currentTimeMillis()
            repeat(iterations) {
                val encoded = str.encodeToString(Encoding.BASE64)
                encoded.decodeToString(Encoding.BASE64)
            }
            val t2 = System.currentTimeMillis()
            repeat(iterations) {
                val encoded = str.encodeToString(Encoding.HEX)
                encoded.decodeToString(Encoding.HEX)
            }
            val t3 = System.currentTimeMillis()
            assertTrue(t2 > t1 && t3 > t2)
        }
    }

    @Nested
    @DisplayName("不同编码类型对比")
    inner class EncodingComparison {
        @Test
        @DisplayName("Base64与Hex编码结果不同且均可解码")
        fun testEncodingComparison() {
            val str = "Hello, World! 你好，世界！"
            val base64 = str.encodeToString(Encoding.BASE64)
            val hex = str.encodeToString(Encoding.HEX)
            assertNotEquals(base64, hex)
            assertEquals(str, base64.decodeToString(Encoding.BASE64))
            assertEquals(str, hex.decodeToString(Encoding.HEX))
        }
    }

    @Nested
    @DisplayName("参数化典型用例")
    inner class ParameterizedCases {
        @ParameterizedTest
        @ValueSource(strings = ["abc", "123", "!@#", "中文", "", "A", "AB", "ABC", "A B C", "\n\t\r"])
        @DisplayName("参数化字符串Base64/Hex编码解码")
        fun testParamCodec(str: String) {
            assertEquals(str, str.encodeToString(Encoding.BASE64).decodeToString(Encoding.BASE64))
            assertEquals(str, str.encodeToString(Encoding.HEX).decodeToString(Encoding.HEX))
        }
    }
}
