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

package tony.test.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import tony.jackson.initialize
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.Date

/**
 * Jackson工具类单元测试
 * @author AI
 * @date 2024/06/09
 * @since 1.0.0
 */
object JacksonTest {

    private val logger = LoggerFactory.getLogger(JacksonTest::class.java)

    // 测试用的数据类
    data class TestPerson(
        val name: String,
        val age: Int,
        val email: String?,
        val score: Double,
        val bigDecimal: BigDecimal,
        val bigInteger: BigInteger,
        val date: Date,
        val localDateTime: LocalDateTime
    )

    @Test
    @DisplayName("基本类型序列化测试")
    fun testBasicTypeSerialization() {
        logger.info("测试基本类型序列化")

        val objectMapper = ObjectMapper().initialize()

        // 测试字符串序列化
        val stringValue = "test string"
        val stringJson = objectMapper.writeValueAsString(stringValue)
        logger.info("字符串序列化结果: {}", stringJson)
        assertEquals("\"test string\"", stringJson)

        // 测试数字序列化
        val intValue = 123
        val intJson = objectMapper.writeValueAsString(intValue)
        logger.info("整数序列化结果: {}", intJson)
        assertEquals("123", intJson)

        // 测试布尔值序列化
        val boolValue = true
        val boolJson = objectMapper.writeValueAsString(boolValue)
        logger.info("布尔值序列化结果: {}", boolJson)
        assertEquals("true", boolJson)
    }

    @Test
    @DisplayName("复杂对象序列化测试")
    fun testComplexObjectSerialization() {
        logger.info("测试复杂对象序列化")

        val objectMapper = ObjectMapper().initialize()

        val person = TestPerson(
            name = "Tony",
            age = 25,
            email = "tony@example.com",
            score = 95.5,
            bigDecimal = BigDecimal("123.456"),
            bigInteger = BigInteger("123456789"),
            date = Date(),
            localDateTime = LocalDateTime.now()
        )

        val json = objectMapper.writeValueAsString(person)
        logger.info("复杂对象序列化结果: {}", json)

        assertNotNull(json)
        assertTrue(json.contains("Tony"))
        assertTrue(json.contains("25"))
        assertTrue(json.contains("tony@example.com"))
        assertTrue(json.contains("95.5"))
        assertTrue(json.contains("123.456"))
        assertTrue(json.contains("123456789"))
    }

    @Test
    @DisplayName("对象反序列化测试")
    fun testObjectDeserialization() {
        logger.info("测试对象反序列化")

        val objectMapper = ObjectMapper().initialize()

        val json = """
            {
                "name": "Tony",
                "age": 25,
                "email": "tony@example.com",
                "score": 95.5,
                "bigDecimal": "123.456",
                "bigInteger": "123456789",
                "date": "2024-06-09 10:30:00",
                "localDateTime": "2024-06-09 10:30:00"
            }
        """.trimIndent()

        val person = objectMapper.readValue(json, TestPerson::class.java)
        logger.info("反序列化结果: {}", person)

        assertEquals("Tony", person.name)
        assertEquals(25, person.age)
        assertEquals("tony@example.com", person.email)
        assertEquals(95.5, person.score)
        assertEquals(BigDecimal("123.456"), person.bigDecimal)
        assertEquals(BigInteger("123456789"), person.bigInteger)
    }

    @Test
    @DisplayName("null值处理测试")
    fun testNullValueHandling() {
        logger.info("测试null值处理")

        val objectMapper = ObjectMapper().initialize()

        val person = TestPerson(
            name = "Tony",
            age = 25,
            email = null,
            score = 95.5,
            bigDecimal = BigDecimal("123.456"),
            bigInteger = BigInteger("123456789"),
            date = Date(),
            localDateTime = LocalDateTime.now()
        )

        val json = objectMapper.writeValueAsString(person)
        logger.info("包含null值的对象序列化结果: {}", json)

        assertTrue(json.contains("null"))
        assertTrue(json.contains("Tony"))

        // 测试反序列化
        val deserializedPerson = objectMapper.readValue(json, TestPerson::class.java)
        logger.info("反序列化结果: {}", deserializedPerson)

        assertEquals("Tony", deserializedPerson.name)
        assertNull(deserializedPerson.email)
    }

    @Test
    @DisplayName("日期时间格式化测试")
    fun testDateTimeFormatting() {
        logger.info("测试日期时间格式化")

        val objectMapper = ObjectMapper().initialize()

        val testDate = Date(120, 5, 9, 10, 30, 0) // 2020-06-09 10:30:00
        val testLocalDateTime = LocalDateTime.of(2020, 6, 9, 10, 30, 0)

        val person = TestPerson(
            name = "Tony",
            age = 25,
            email = "tony@example.com",
            score = 95.5,
            bigDecimal = BigDecimal("123.456"),
            bigInteger = BigInteger("123456789"),
            date = testDate,
            localDateTime = testLocalDateTime
        )

        val json = objectMapper.writeValueAsString(person)
        logger.info("日期时间序列化结果: {}", json)

        // 验证日期格式
        assertTrue(json.contains("2020-06-09 10:30:00"))

        // 测试反序列化
        val deserializedPerson = objectMapper.readValue(json, TestPerson::class.java)
        logger.info("日期时间反序列化结果: {}", deserializedPerson)

        assertNotNull(deserializedPerson.date)
        assertNotNull(deserializedPerson.localDateTime)
    }

    @Test
    @DisplayName("大数字格式化测试")
    fun testBigNumberFormatting() {
        logger.info("测试大数字格式化")

        val objectMapper = ObjectMapper().initialize()

        val person = TestPerson(
            name = "Tony",
            age = 25,
            email = "tony@example.com",
            score = 95.5,
            bigDecimal = BigDecimal("123456789.123456789"),
            bigInteger = BigInteger("1234567890123456789"),
            date = Date(),
            localDateTime = LocalDateTime.now()
        )

        val json = objectMapper.writeValueAsString(person)
        logger.info("大数字序列化结果: {}", json)

        // 验证大数字被序列化为字符串
        assertTrue(json.contains("\"123456789.123456789\""))
        assertTrue(json.contains("\"1234567890123456789\""))

        // 测试反序列化
        val deserializedPerson = objectMapper.readValue(json, TestPerson::class.java)
        logger.info("大数字反序列化结果: {}", deserializedPerson)

        assertEquals(BigDecimal("123456789.123456789"), deserializedPerson.bigDecimal)
        assertEquals(BigInteger("1234567890123456789"), deserializedPerson.bigInteger)
    }

    @Test
    @DisplayName("Kotlin特性支持测试")
    fun testKotlinFeatures() {
        logger.info("测试Kotlin特性支持")

        val objectMapper = ObjectMapper().initialize()

        // 测试Kotlin数据类
        data class KotlinPerson(
            val name: String,
            val age: Int,
            val email: String? = null,
            val scores: List<Int> = emptyList()
        )

        val person = KotlinPerson(
            name = "Tony",
            age = 25,
            email = null,
            scores = listOf(90, 95, 88)
        )

        val json = objectMapper.writeValueAsString(person)
        logger.info("Kotlin数据类序列化结果: {}", json)

        assertTrue(json.contains("Tony"))
        assertTrue(json.contains("25"))
        assertTrue(json.contains("[90,95,88]"))

        // 测试反序列化
        val deserializedPerson = objectMapper.readValue(json, KotlinPerson::class.java)
        logger.info("Kotlin数据类反序列化结果: {}", deserializedPerson)

        assertEquals("Tony", deserializedPerson.name)
        assertEquals(25, deserializedPerson.age)
        assertEquals(listOf(90, 95, 88), deserializedPerson.scores)
    }

    @Test
    @DisplayName("集合类型序列化测试")
    fun testCollectionSerialization() {
        logger.info("测试集合类型序列化")

        val objectMapper = ObjectMapper().initialize()

        val list = listOf(1, 2, 3, 4, 5)
        val listJson = objectMapper.writeValueAsString(list)
        logger.info("List序列化结果: {}", listJson)
        assertEquals("[1,2,3,4,5]", listJson)

        val map = mapOf("name" to "Tony", "age" to 25)
        val mapJson = objectMapper.writeValueAsString(map)
        logger.info("Map序列化结果: {}", mapJson)
        assertTrue(mapJson.contains("Tony"))
        assertTrue(mapJson.contains("25"))

        // 测试反序列化
        val deserializedList = objectMapper.readValue(listJson, List::class.java)
        val deserializedMap = objectMapper.readValue(mapJson, Map::class.java)

        logger.info("List反序列化结果: {}", deserializedList)
        logger.info("Map反序列化结果: {}", deserializedMap)

        assertEquals(5, deserializedList.size)
        assertEquals(2, deserializedMap.size)
    }

    @Test
    @DisplayName("错误处理测试")
    fun testErrorHandling() {
        logger.info("测试错误处理")

        val objectMapper = ObjectMapper().initialize()

        // 测试无效JSON
        val invalidJson = "{ invalid json }"

        assertThrows(Exception::class.java) {
            objectMapper.readValue(invalidJson, TestPerson::class.java)
        }
        logger.info("无效JSON正确抛出异常")

        // 测试缺少必需字段
        val incompleteJson = """
            {
                "name": "Tony"
            }
        """.trimIndent()

        assertThrows(Exception::class.java) {
            objectMapper.readValue(incompleteJson, TestPerson::class.java)
        }
        logger.info("缺少必需字段正确抛出异常")
    }

    @Test
    @DisplayName("性能测试")
    fun testPerformance() {
        logger.info("测试性能")

        val objectMapper = ObjectMapper().initialize()
        val person = TestPerson(
            name = "Tony",
            age = 25,
            email = "tony@example.com",
            score = 95.5,
            bigDecimal = BigDecimal("123.456"),
            bigInteger = BigInteger("123456789"),
            date = Date(),
            localDateTime = LocalDateTime.now()
        )

        val iterations = 10000
        val startTime = System.currentTimeMillis()

        repeat(iterations) {
            objectMapper.writeValueAsString(person)
        }

        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        logger.info("执行{}次序列化耗时: {}ms", iterations, duration)
        logger.info("平均每次序列化耗时: {}ms", duration.toDouble() / iterations)
    }
}
