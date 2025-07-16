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

package tony.test.redis

import jakarta.annotation.Resource
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import tony.core.enums.IntEnumValue
import tony.core.enums.StringEnumValue
import tony.core.utils.jsonToObj
import tony.core.utils.toJsonString
import tony.redis.RedisManager
import tony.redis.service.RedisService
import tony.test.redis.util.TestDataGenerator

/**
 * Redis 测试基类
 *
 * 提供通用的测试基础设施和验证方法
 *
 * @author tony
 * @date 2025/07/01 17:00
 */
@SpringBootTest(
    classes = [TestRedisApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@ActiveProfiles("test")
@TestPropertySource(locations = ["classpath:application.yml"])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseRedisTest {

    private val logger = LoggerFactory.getLogger(BaseRedisTest::class.java)

    @Resource
    protected lateinit var redisService: RedisService

    /**
     * 测试前的准备工作
     */
    @BeforeEach
    fun setUp() {
        // 清理Redis测试数据
        cleanupTestData()
    }

    /**
     * 清理测试数据
     */
    protected fun cleanupTestData() {
        try {
            // 删除所有以 test: 开头的键
            val keys = RedisManager.keys("test:*")
            if (keys.isNotEmpty()) {
                RedisManager.delete(keys)
            }
        } catch (e: Exception) {
            // 忽略清理过程中的异常
            logger.warn("Cleanup test data failed: {}", e.message)
        }
    }

    /**
     * 生成测试键
     */
    protected fun generateTestKey(prefix: String = "test"): String =
        "$prefix:${System.currentTimeMillis()}:${Thread.currentThread().id}"

    /**
     * 生成测试键列表
     */
    protected fun generateTestKeys(count: Int, prefix: String = "test"): List<String> =
        (1..count).map { generateTestKey("$prefix$it") }

    /**
     * 获取测试数据生成器（静态调用）
     */
    protected fun getTestDataGenerator(): TestDataGenerator = TestDataGenerator

    /**
     * 验证对象序列化和反序列化的正确性（使用tony-core的JSON工具）
     */
    protected fun <T> verifySerializationDeserialization(
        original: T,
        clazz: Class<T>,
        message: String = "序列化和反序列化应该保持对象一致性"
    ) {
        try {
            val json = original.toJsonString()
            val deserialized = json.jsonToObj(clazz)

            assert(original == deserialized) {
                "$message: 原始对象与反序列化对象不一致\n" +
                    "原始对象: $original\n" +
                    "反序列化对象: $deserialized\n" +
                    "JSON: $json"
            }
        } catch (e: Exception) {
            throw AssertionError("$message: 序列化或反序列化失败", e)
        }
    }

    /**
     * 验证枚举类型处理
     */
    protected fun <T : IntEnumValue> verifyIntEnumHandling(
        key: String,
        enumValue: T,
        enumClass: Class<T>,
        message: String = "整数枚举处理应该正确"
    ) {
        try {
            // 设置枚举值
            redisService.set(key, enumValue)

            // 获取枚举值
            val retrieved = redisService.get(key, enumClass)

            assert(retrieved == enumValue) {
                "$message: 整数枚举处理不正确\n" +
                    "原始枚举: $enumValue (${enumValue.value})\n" +
                    "获取枚举: $retrieved"
            }

            // 验证枚举值正确
            assert(retrieved?.value == enumValue.value) {
                "$message: 整数枚举值不正确\n" +
                    "期望值: ${enumValue.value}\n" +
                    "实际值: ${retrieved?.value}"
            }
        } catch (e: Exception) {
            throw AssertionError("$message: 整数枚举处理失败", e)
        }
    }

    /**
     * 验证字符串枚举处理
     */
    protected fun <T : StringEnumValue> verifyStringEnumHandling(
        key: String,
        enumValue: T,
        enumClass: Class<T>,
        message: String = "字符串枚举处理应该正确"
    ) {
        try {
            // 设置枚举值
            redisService.set(key, enumValue)

            // 获取枚举值 - 明确指定类型参数
            val retrieved: T? = redisService.get(key, enumClass)

            assert(retrieved == enumValue) {
                "$message: 字符串枚举处理不正确\n" +
                    "原始枚举: $enumValue (${enumValue.value})\n" +
                    "获取枚举: $retrieved"
            }

            // 验证枚举值正确
            assert(retrieved?.value == enumValue.value) {
                "$message: 字符串枚举值不正确\n" +
                    "期望值: ${enumValue.value}\n" +
                    "实际值: ${retrieved?.value}"
            }
        } catch (e: Exception) {
            throw AssertionError("$message: 字符串枚举处理失败", e)
        }
    }

    /**
     * 验证复杂对象处理
     */
    protected fun <T : Any> verifyComplexObjectHandling(
        key: String,
        original: T,
        clazz: Class<T>,
        message: String = "复杂对象处理应该正确"
    ) {
        try {
            // 设置复杂对象 - 将T转换为Any
            redisService.set(key, original)

            // 获取复杂对象 - 明确指定类型参数
            val retrieved = redisService.get(key, clazz)

            assert(retrieved == original) {
                "$message: 复杂对象处理不正确\n" +
                    "原始对象: $original\n" +
                    "获取对象: $retrieved"
            }

            // 验证序列化反序列化一致性
            verifySerializationDeserialization(original, clazz, message)
        } catch (e: Exception) {
            throw AssertionError("$message: 复杂对象处理失败", e)
        }
    }
}
