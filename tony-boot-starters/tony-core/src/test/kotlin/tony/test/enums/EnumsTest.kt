package tony.test.enums

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import tony.enums.*

/**
 * 枚举工具类与扩展测试
 *
 * @author tangli
 * @since 1.0.0
 */
@DisplayName("枚举工具类与扩展测试")
class EnumsTest {

    // 测试用枚举
    enum class TestIntEnum(override val value: Int) : IntEnumValue {
        ONE(1), TWO(2), THREE(3), DEFAULT(DEFAULT_INT_VALUE);
        companion object : IntEnumCreator(TestIntEnum::class.java)
    }
    enum class TestStringEnum(override val value: String) : StringEnumValue {
        HELLO("hello"), WORLD("world"), TONY("tony"), DEFAULT(DEFAULT_STRING_VALUE);
        companion object : StringEnumCreator(TestStringEnum::class.java) {
            override fun create(value: String): StringEnumValue? {
                return super.create(value.lowercase())
            }
        }
    }

    @Nested
    @DisplayName("IntEnumValue/StringEnumValue 接口")
    inner class EnumValueInterface {
        @Test
        @DisplayName("IntEnumValue接口功能")
        fun testIntEnumValue() {
            assertEquals(1, TestIntEnum.ONE.value)
            assertEquals(2, TestIntEnum.TWO.value)
            assertEquals(DEFAULT_INT_VALUE, TestIntEnum.DEFAULT.value)
            assertTrue(TestIntEnum.ONE is IntEnumValue)
        }
        @Test
        @DisplayName("StringEnumValue接口功能")
        fun testStringEnumValue() {
            assertEquals("hello", TestStringEnum.HELLO.value)
            assertEquals("world", TestStringEnum.WORLD.value)
            assertEquals(DEFAULT_STRING_VALUE, TestStringEnum.DEFAULT.value)
            assertTrue(TestStringEnum.HELLO is StringEnumValue)
        }
    }

    @Nested
    @DisplayName("IntEnumCreator/StringEnumCreator/EnumCreator")
    inner class EnumCreatorTest {
        @Test
        @DisplayName("IntEnumCreator创建器")
        fun testIntEnumCreator() {
            assertEquals(TestIntEnum.ONE, TestIntEnum.create(1))
            assertEquals(TestIntEnum.TWO, TestIntEnum.create(2))
            assertEquals(TestIntEnum.THREE, TestIntEnum.create(3))
            assertNull(TestIntEnum.create(999))
            assertEquals(TestIntEnum.DEFAULT, TestIntEnum.create(DEFAULT_INT_VALUE))
        }
        @Test
        @DisplayName("StringEnumCreator创建器")
        fun testStringEnumCreator() {
            assertEquals(TestStringEnum.HELLO, TestStringEnum.create("hello"))
            assertEquals(TestStringEnum.WORLD, TestStringEnum.create("world"))
            assertEquals(TestStringEnum.TONY, TestStringEnum.create("tony"))
            assertNull(TestStringEnum.create("invalid"))
            assertEquals(TestStringEnum.DEFAULT, TestStringEnum.create(DEFAULT_STRING_VALUE))
            // 大小写不敏感
            assertEquals(TestStringEnum.HELLO, TestStringEnum.create("HELLO"))
        }
        @Test
        @DisplayName("EnumCreator基类功能")
        fun testEnumCreatorBase() {
            val intCreator = EnumCreator.creatorOf<TestIntEnum, Int>(TestIntEnum::class.java)
            val stringCreator = EnumCreator.creatorOf<TestStringEnum, String>(TestStringEnum::class.java)
            assertNotNull(intCreator)
            assertNotNull(stringCreator)
            assertEquals(TestIntEnum.ONE, intCreator.create(1))
            assertEquals(TestStringEnum.HELLO, stringCreator.create("hello"))
        }
    }

    @Nested
    @DisplayName("常量与唯一性")
    inner class ConstAndUniqueness {
        @Test
        @DisplayName("常量值校验")
        fun testConstants() {
            assertEquals(-1, DEFAULT_INT_VALUE)
            assertEquals("", DEFAULT_STRING_VALUE)
            assertEquals(DEFAULT_INT_VALUE, TestIntEnum.DEFAULT.value)
            assertEquals(DEFAULT_STRING_VALUE, TestStringEnum.DEFAULT.value)
        }
        @Test
        @DisplayName("枚举值唯一性")
        fun testEnumValueUniqueness() {
            val intValues = TestIntEnum.values().map { it.value }
            val stringValues = TestStringEnum.values().map { it.value }
            assertEquals(intValues.size, intValues.toSet().size)
            assertEquals(stringValues.size, stringValues.toSet().size)
        }
    }

    @Nested
    @DisplayName("序列化与边界")
    inner class SerializationAndEdge {
        @Test
        @DisplayName("枚举序列化/反序列化")
        fun testEnumSerialization() {
            val intEnum = TestIntEnum.ONE
            val intValue = intEnum.value
            assertEquals(intEnum, TestIntEnum.create(intValue))
            val stringEnum = TestStringEnum.HELLO
            val stringValue = stringEnum.value
            assertEquals(stringEnum, TestStringEnum.create(stringValue))
        }
        @Test
        @DisplayName("边界情况")
        fun testEdgeCases() {
            assertEquals(TestStringEnum.DEFAULT, TestStringEnum.create(""))
            assertNull(TestIntEnum.create(0)) // 假设0不是有效值
        }
    }

    @Nested
    @DisplayName("性能与并发")
    inner class PerformanceAndConcurrency {
        @Test
        @DisplayName("性能测试")
        fun testPerformance() {
            val iterations = 10000
            val t1 = System.currentTimeMillis()
            repeat(iterations) {
                TestIntEnum.create(1)
                TestStringEnum.create("hello")
            }
            val t2 = System.currentTimeMillis()
            assertTrue(t2 > t1)
        }
        @Test
        @DisplayName("并发安全测试")
        fun testConcurrency() {
            val threadCount = 10
            val iterationsPerThread = 1000
            val threads = mutableListOf<Thread>()
            repeat(threadCount) {
                threads += Thread {
                    repeat(iterationsPerThread) {
                        assertEquals(TestIntEnum.ONE, TestIntEnum.create(1))
                        assertEquals(TestStringEnum.HELLO, TestStringEnum.create("hello"))
                    }
                }.apply { start() }
            }
            threads.forEach { it.join() }
        }
    }

    @Nested
    @DisplayName("参数化典型用例")
    inner class ParameterizedCases {
        @ParameterizedTest
        @ValueSource(ints = [1, 2, 3, -1, 0, 999])
        @DisplayName("参数化IntEnum创建")
        fun testParamIntEnumCreate(value: Int) {
            val result = TestIntEnum.create(value)
            if (value in 1..3) {
                assertNotNull(result)
            } else if (value == DEFAULT_INT_VALUE) {
                assertEquals(TestIntEnum.DEFAULT, result)
            } else {
                assertNull(result)
            }
        }
        @ParameterizedTest
        @ValueSource(strings = ["hello", "world", "tony", "", "HELLO", "invalid"])
        @DisplayName("参数化StringEnum创建")
        fun testParamStringEnumCreate(value: String) {
            val result = TestStringEnum.create(value)
            if (value.lowercase() in listOf("hello", "world", "tony")) {
                assertNotNull(result)
            } else if (value == DEFAULT_STRING_VALUE) {
                assertEquals(TestStringEnum.DEFAULT, result)
            } else {
                assertNull(result)
            }
        }
    }
}
