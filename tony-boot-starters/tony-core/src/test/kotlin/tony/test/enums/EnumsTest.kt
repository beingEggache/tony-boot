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

package tony.test.enums

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import tony.enums.*

/**
 * 枚举工具类单元测试
 * @author AI
 * @date 2024/06/09
 * @since 1.0.0
 */
object EnumsTest {

    private val logger = LoggerFactory.getLogger(EnumsTest::class.java)

    // 测试用的枚举类
    enum class TestIntEnum(override val value: Int) : IntEnumValue {
        ONE(1),
        TWO(2),
        THREE(3),
        DEFAULT(DEFAULT_INT_VALUE);

        companion object : IntEnumCreator(TestIntEnum::class.java) {
            @JvmStatic
            override fun create(value: Int): IntEnumValue? = super.create(value)
        }
    }

    enum class TestStringEnum(override val value: String) : StringEnumValue {
        HELLO("hello"),
        WORLD("world"),
        TONY("tony"),
        DEFAULT(DEFAULT_STRING_VALUE);

        companion object : StringEnumCreator(TestStringEnum::class.java) {
            @JvmStatic
            override fun create(value: String): StringEnumValue? = super.create(value)
        }
    }

    @Test
    @DisplayName("IntEnumValue接口测试")
    fun testIntEnumValue() {
        logger.info("测试IntEnumValue接口")

        val enum1 = TestIntEnum.ONE
        val enum2 = TestIntEnum.TWO
        val enum3 = TestIntEnum.DEFAULT

        logger.info("枚举ONE的值: {}", enum1.value)
        logger.info("枚举TWO的值: {}", enum2.value)
        logger.info("枚举DEFAULT的值: {}", enum3.value)

        assertEquals(1, enum1.value)
        assertEquals(2, enum2.value)
        assertEquals(DEFAULT_INT_VALUE, enum3.value)

        // 测试接口类型
        assertTrue(enum1 is IntEnumValue)
        assertTrue(enum2 is IntEnumValue)
        assertTrue(enum3 is IntEnumValue)
    }

    @Test
    @DisplayName("StringEnumValue接口测试")
    fun testStringEnumValue() {
        logger.info("测试StringEnumValue接口")

        val enum1 = TestStringEnum.HELLO
        val enum2 = TestStringEnum.WORLD
        val enum3 = TestStringEnum.DEFAULT

        logger.info("枚举HELLO的值: {}", enum1.value)
        logger.info("枚举WORLD的值: {}", enum2.value)
        logger.info("枚举DEFAULT的值: {}", enum3.value)

        assertEquals("hello", enum1.value)
        assertEquals("world", enum2.value)
        assertEquals(DEFAULT_STRING_VALUE, enum3.value)

        // 测试接口类型
        assertTrue(enum1 is StringEnumValue)
        assertTrue(enum2 is StringEnumValue)
        assertTrue(enum3 is StringEnumValue)
    }

    @Test
    @DisplayName("IntEnumCreator创建器测试")
    fun testIntEnumCreator() {
        logger.info("测试IntEnumCreator创建器")

        // 测试有效值创建
        val enum1 = TestIntEnum.create(1)
        val enum2 = TestIntEnum.create(2)
        val enum3 = TestIntEnum.create(3)

        logger.info("通过值1创建的枚举: {}", enum1)
        logger.info("通过值2创建的枚举: {}", enum2)
        logger.info("通过值3创建的枚举: {}", enum3)

        assertEquals(TestIntEnum.ONE, enum1)
        assertEquals(TestIntEnum.TWO, enum2)
        assertEquals(TestIntEnum.THREE, enum3)

        // 测试无效值创建
        val invalidEnum = TestIntEnum.create(999)
        logger.info("通过无效值999创建的枚举: {}", invalidEnum)
        assertNull(invalidEnum)

        // 测试默认值创建
        val defaultEnum = TestIntEnum.create(DEFAULT_INT_VALUE)
        logger.info("通过默认值创建的枚举: {}", defaultEnum)
        assertEquals(TestIntEnum.DEFAULT, defaultEnum)
    }

    @Test
    @DisplayName("StringEnumCreator创建器测试")
    fun testStringEnumCreator() {
        logger.info("测试StringEnumCreator创建器")

        // 测试有效值创建
        val enum1 = TestStringEnum.create("hello")
        val enum2 = TestStringEnum.create("world")
        val enum3 = TestStringEnum.create("tony")

        logger.info("通过值'hello'创建的枚举: {}", enum1)
        logger.info("通过值'world'创建的枚举: {}", enum2)
        logger.info("通过值'tony'创建的枚举: {}", enum3)

        assertEquals(TestStringEnum.HELLO, enum1)
        assertEquals(TestStringEnum.WORLD, enum2)
        assertEquals(TestStringEnum.TONY, enum3)

        // 测试无效值创建
        val invalidEnum = TestStringEnum.create("invalid")
        logger.info("通过无效值'invalid'创建的枚举: {}", invalidEnum)
        assertNull(invalidEnum)

        // 测试默认值创建
        val defaultEnum = TestStringEnum.create(DEFAULT_STRING_VALUE)
        logger.info("通过默认值创建的枚举: {}", defaultEnum)
        assertEquals(TestStringEnum.DEFAULT, defaultEnum)

        // 测试大小写不敏感
        val upperEnum = TestStringEnum.create("HELLO")
        logger.info("通过大写值'HELLO'创建的枚举: {}", upperEnum)
        assertEquals(TestStringEnum.HELLO, upperEnum)
    }

    @Test
    @DisplayName("EnumCreator基类测试")
    fun testEnumCreator() {
        logger.info("测试EnumCreator基类")

        // 测试通过类获取创建器
        val intCreator = EnumCreator.creatorOf<TestIntEnum, Int>(TestIntEnum::class.java)
        val stringCreator = EnumCreator.creatorOf<TestStringEnum, String>(TestStringEnum::class.java)

        logger.info("Int枚举创建器: {}", intCreator::class.simpleName)
        logger.info("String枚举创建器: {}", stringCreator::class.simpleName)

        assertNotNull(intCreator)
        assertNotNull(stringCreator)

        // 测试创建器功能
        val createdInt = intCreator.create(1)
        val createdString = stringCreator.create("hello")

        logger.info("通过创建器创建的Int枚举: {}", createdInt)
        logger.info("通过创建器创建的String枚举: {}", createdString)

        assertEquals(TestIntEnum.ONE, createdInt)
        assertEquals(TestStringEnum.HELLO, createdString)
    }

    @Test
    @DisplayName("常量值测试")
    fun testConstants() {
        logger.info("测试常量值")

        logger.info("DEFAULT_INT_VALUE: {}", DEFAULT_INT_VALUE)
        logger.info("DEFAULT_STRING_VALUE: '{}'", DEFAULT_STRING_VALUE)

        assertEquals(-1, DEFAULT_INT_VALUE)
        assertEquals("", DEFAULT_STRING_VALUE)

        // 测试常量在枚举中的使用
        val defaultIntEnum = TestIntEnum.DEFAULT
        val defaultStringEnum = TestStringEnum.DEFAULT

        assertEquals(DEFAULT_INT_VALUE, defaultIntEnum.value)
        assertEquals(DEFAULT_STRING_VALUE, defaultStringEnum.value)

        logger.info("默认Int枚举值: {}", defaultIntEnum.value)
        logger.info("默认String枚举值: '{}'", defaultStringEnum.value)
    }

    @Test
    @DisplayName("枚举值唯一性测试")
    fun testEnumValueUniqueness() {
        logger.info("测试枚举值唯一性")

        val intValues = TestIntEnum.values().map { it.value }
        val stringValues = TestStringEnum.values().map { it.value }

        logger.info("Int枚举所有值: {}", intValues)
        logger.info("String枚举所有值: {}", stringValues)

        // 检查值是否唯一
        assertEquals(intValues.size, intValues.toSet().size)
        assertEquals(stringValues.size, stringValues.toSet().size)

        logger.info("Int枚举值唯一性检查通过")
        logger.info("String枚举值唯一性检查通过")
    }

    @Test
    @DisplayName("枚举序列化测试")
    fun testEnumSerialization() {
        logger.info("测试枚举序列化")

        // 测试Int枚举序列化
        val intEnum = TestIntEnum.ONE
        val intValue = intEnum.value
        val deserializedInt = TestIntEnum.create(intValue)

        logger.info("原始Int枚举: {}", intEnum)
        logger.info("序列化后的值: {}", intValue)
        logger.info("反序列化后的枚举: {}", deserializedInt)

        assertEquals(intEnum, deserializedInt)

        // 测试String枚举序列化
        val stringEnum = TestStringEnum.HELLO
        val stringValue = stringEnum.value
        val deserializedString = TestStringEnum.create(stringValue)

        logger.info("原始String枚举: {}", stringEnum)
        logger.info("序列化后的值: {}", stringValue)
        logger.info("反序列化后的枚举: {}", deserializedString)

        assertEquals(stringEnum, deserializedString)
    }

    @Test
    @DisplayName("边界情况测试")
    fun testEdgeCases() {
        logger.info("测试边界情况")

        // 测试空字符串创建
        val emptyStringEnum = TestStringEnum.create("")
        logger.info("通过空字符串创建的枚举: {}", emptyStringEnum)
        assertEquals(TestStringEnum.DEFAULT, emptyStringEnum)

        // 测试零值创建
        val zeroIntEnum = TestIntEnum.create(0)
        logger.info("通过零值创建的Int枚举: {}", zeroIntEnum)
        assertNull(zeroIntEnum) // 假设0不是有效值
    }

    @Test
    @DisplayName("性能测试")
    fun testPerformance() {
        logger.info("测试性能")

        val iterations = 10000
        val startTime = System.currentTimeMillis()

        repeat(iterations) {
            TestIntEnum.create(1)
            TestStringEnum.create("hello")
        }

        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        logger.info("执行{}次枚举创建耗时: {}ms", iterations * 2, duration)
        logger.info("平均每次创建耗时: {}ms", duration.toDouble() / (iterations * 2))
    }

    @Test
    @DisplayName("并发安全测试")
    fun testConcurrency() {
        logger.info("测试并发安全")

        val threadCount = 10
        val iterationsPerThread = 1000
        val threads = mutableListOf<Thread>()

        val startTime = System.currentTimeMillis()

        repeat(threadCount) { threadIndex ->
            val thread = Thread {
                repeat(iterationsPerThread) { iteration ->
                    val intEnum = TestIntEnum.create(1)
                    val stringEnum = TestStringEnum.create("hello")

                    assertEquals(TestIntEnum.ONE, intEnum)
                    assertEquals(TestStringEnum.HELLO, stringEnum)
                }
            }
            threads.add(thread)
            thread.start()
        }

        threads.forEach { it.join() }

        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        logger.info("{}个线程并发执行{}次枚举创建耗时: {}ms", threadCount, iterationsPerThread, duration)
        logger.info("总执行次数: {}", threadCount * iterationsPerThread * 2)
    }
}
