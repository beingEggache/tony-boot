package tony.test.codec

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import tony.codec.Base64Codec
import tony.codec.Codec
import tony.codec.HexCodec

/**
 * Codec 接口及实现测试
 *
 * @author tangli
 * @date 2025/06/27 17:00
 */
@DisplayName("Codec 接口及实现测试")
class CodecTest {

    @Nested
    @DisplayName("Codec 实例与类型")
    inner class CodecInstances {
        @Test
        @DisplayName("Base64Codec/HexCodec 实例校验")
        fun testCodecInstances() {
            val base64 = Base64Codec
            val hex = HexCodec
            assertNotNull(base64)
            assertNotNull(hex)
            assertTrue(base64 is Codec)
            assertTrue(hex is Codec)
        }
    }

    @Nested
    @DisplayName("Base64 编解码功能")
    inner class Base64CodecFeature {
        @Test
        @DisplayName("字符串Base64编解码")
        fun testStringEncodeDecode() {
            val str = "Hello, World! 你好，世界！"
            val encoded = Base64Codec.encodeToString(str)
            val decoded = Base64Codec.decodeToString(encoded)
            assertEquals(str, decoded)
        }

        @Test
        @DisplayName("字节数组Base64编解码")
        fun testByteArrayEncodeDecode() {
            val bytes = "Test Data 测试".toByteArray(Charsets.UTF_8)
            val encoded = Base64Codec.encodeToString(bytes)
            val decoded = Base64Codec.decodeToByteArray(encoded)
            assertArrayEquals(bytes, decoded)
        }
    }

    @Nested
    @DisplayName("Hex 编解码功能")
    inner class HexCodecFeature {
        @Test
        @DisplayName("字符串Hex编解码")
        fun testStringEncodeDecode() {
            val str = "Hello, World! 你好，世界！"
            val encoded = HexCodec.encodeToString(str)
            val decoded = HexCodec.decodeToString(encoded)
            assertEquals(str, decoded)
        }

        @Test
        @DisplayName("字节数组Hex编解码")
        fun testByteArrayEncodeDecode() {
            val bytes = "Test Data 测试".toByteArray(Charsets.UTF_8)
            val encoded = HexCodec.encodeToString(bytes)
            val decoded = HexCodec.decodeToByteArray(encoded)
            assertArrayEquals(bytes, decoded)
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
            assertEquals(emptyStr, Base64Codec.decodeToString(Base64Codec.encodeToString(emptyStr)))
            assertEquals(emptyStr, HexCodec.decodeToString(HexCodec.encodeToString(emptyStr)))
            assertArrayEquals(emptyBytes, Base64Codec.decodeToByteArray(Base64Codec.encodeToString(emptyBytes)))
            assertArrayEquals(emptyBytes, HexCodec.decodeToByteArray(HexCodec.encodeToString(emptyBytes)))
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
            assertEquals(one, HexCodec.decodeToString(HexCodec.encodeToString(one)))
            assertEquals(two, HexCodec.decodeToString(HexCodec.encodeToString(two)))
            assertEquals(three, HexCodec.decodeToString(HexCodec.encodeToString(three)))
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
            assertEquals(special, HexCodec.decodeToString(HexCodec.encodeToString(special)))
        }

        @Test
        @DisplayName("中文编码解码")
        fun testChineseCharacters() {
            val chinese = "中文测试：你好世界！"
            assertEquals(chinese, Base64Codec.decodeToString(Base64Codec.encodeToString(chinese)))
            assertEquals(chinese, HexCodec.decodeToString(HexCodec.encodeToString(chinese)))
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
            assertEquals(large, HexCodec.decodeToString(HexCodec.encodeToString(large)))
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
                val encoded = HexCodec.encodeToString(str)
                HexCodec.decodeToString(encoded)
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
        @DisplayName("参数化字符串Base64/Hex编解码")
        fun testParamCodec(str: String) {
            assertEquals(str, Base64Codec.decodeToString(Base64Codec.encodeToString(str)))
            assertEquals(str, HexCodec.decodeToString(HexCodec.encodeToString(str)))
        }
    }
}
