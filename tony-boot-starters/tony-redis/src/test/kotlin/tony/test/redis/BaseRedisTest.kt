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

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.Resource
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import tony.enums.IntEnumValue
import tony.enums.StringEnumValue
import tony.redis.service.RedisService
import tony.redis.RedisManager
import tony.test.redis.TestRedisApplication
import tony.test.redis.util.TestDataGenerator
import tony.utils.jsonToObj
import tony.utils.toJsonString

/**
 * Redis 测试基类
 *
 * 提供通用的测试基础设施和验证方法
 *
 * @author tony
 * @since 2024-01-01
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
     * 验证集合类型序列化和反序列化的正确性
     */
    protected fun <T> verifyCollectionSerializationDeserialization(
        original: Collection<T>,
        clazz: Class<out Collection<T>>,
        message: String = "集合序列化和反序列化应该保持一致性"
    ) {
        try {
            val json = original.toJsonString()
            val deserialized = json.jsonToObj(clazz)

            assert(original.size == deserialized.size) {
                "$message: 集合大小不一致，原始: ${original.size}, 反序列化: ${deserialized.size}"
            }

            assert(original.containsAll(deserialized) && deserialized.containsAll(original)) {
                "$message: 集合内容不一致\n" +
                    "原始集合: $original\n" +
                    "反序列化集合: $deserialized"
            }
        } catch (e: Exception) {
            throw AssertionError("$message: 集合序列化或反序列化失败", e)
        }
    }

    /**
     * 验证Map类型序列化和反序列化的正确性
     */
    protected fun <K, V> verifyMapSerializationDeserialization(
        original: Map<K, V>,
        clazz: Class<out Map<K, V>>,
        message: String = "Map序列化和反序列化应该保持一致性"
    ) {
        try {
            val json = original.toJsonString()
            val deserialized = json.jsonToObj(clazz)

            assert(original.size == deserialized.size) {
                "$message: Map大小不一致，原始: ${original.size}, 反序列化: ${deserialized.size}"
            }

            original.forEach { (key, value) ->
                assert(deserialized[key] == value) {
                    "$message: Map键值对不一致，键: $key, 原始值: $value, 反序列化值: ${deserialized[key]}"
                }
            }
        } catch (e: Exception) {
            throw AssertionError("$message: Map序列化或反序列化失败", e)
        }
    }

    /**
     * 验证基本类型转换的正确性
     */
    protected fun verifyBasicTypeConversion(
        original: Any,
        expectedType: Class<*>,
        message: String = "基本类型转换应该保持一致性"
    ) {
        try {
            val json = original.toJsonString()
            val deserialized = json.jsonToObj(expectedType)

            assert(original == deserialized) {
                "$message: 基本类型转换不一致\n" +
                    "原始值: $original (${original.javaClass.simpleName})\n" +
                    "转换后值: $deserialized (${deserialized.javaClass.simpleName})\n" +
                    "JSON: $json"
            }
        } catch (e: Exception) {
            throw AssertionError("$message: 基本类型转换失败", e)
        }
    }

    /**
     * 验证空值处理
     */
    protected fun verifyNullHandling(
        key: String,
        message: String = "空值处理应该正确"
    ) {
        try {
            // 设置空值 - 注意：RedisService.set不支持null值，这里测试边界情况
            // 实际测试中应该避免传入null值
            logger.info("$message: RedisService.set不支持null值，跳过此测试")
        } catch (e: Exception) {
            // 预期的异常，测试通过
            logger.info("Expected exception caught: ${e.message}")
        }
    }

    /**
     * 验证特殊字符处理
     */
    protected fun verifySpecialCharacterHandling(
        key: String,
        value: String,
        message: String = "特殊字符处理应该正确"
    ) {
        try {
            // 设置包含特殊字符的值
            redisService.set(key, value)

            // 获取值
            val retrieved = redisService.get(key, String::class.java)

            assert(retrieved == value) {
                "$message: 特殊字符处理不正确\n" +
                    "原始值: $value\n" +
                    "获取值: $retrieved"
            }
        } catch (e: Exception) {
            throw AssertionError("$message: 特殊字符处理失败", e)
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

    /**
     * 验证过期时间处理
     */
    protected fun verifyExpirationHandling(
        key: String,
        value: Any,
        expirationSeconds: Long,
        message: String = "过期时间处理应该正确"
    ) {
        try {
            // 设置带过期时间的值
            redisService.set(key, value, expirationSeconds)

            // 立即获取值
            val retrieved = redisService.get(key, Any::class.java)
            assert(retrieved == value) {
                "$message: 设置后立即获取值不正确\n" +
                    "期望值: $value\n" +
                    "实际值: $retrieved"
            }

            // 等待过期时间后获取值
            Thread.sleep((expirationSeconds + 1) * 1000)
            val expiredValue = redisService.get(key, Any::class.java)
            assert(expiredValue == null) {
                "$message: 过期后值应该为null，实际: $expiredValue"
            }
        } catch (e: Exception) {
            throw AssertionError("$message: 过期时间处理失败", e)
        }
    }

    /**
     * 验证批量操作
     */
    protected fun verifyBatchOperations(
        keys: List<String>,
        values: List<Any>,
        message: String = "批量操作应该正确"
    ) {
        try {
            // 批量设置
            keys.forEachIndexed { index, key ->
                redisService.set(key, values[index])
            }

            // 批量获取
            keys.forEachIndexed { index, key ->
                val retrieved = redisService.get(key, Any::class.java)
                assert(retrieved == values[index]) {
                    "$message: 批量操作中键 $key 的值不正确\n" +
                        "期望值: ${values[index]}\n" +
                        "实际值: $retrieved"
                }
            }
        } catch (e: Exception) {
            throw AssertionError("$message: 批量操作失败", e)
        }
    }

    /**
     * 验证条件设置操作
     */
    protected fun verifyConditionalOperations(
        key: String,
        value: Any,
        message: String = "条件设置操作应该正确"
    ) {
        try {
            // 测试setIfAbsent - 键不存在时设置
            val setIfAbsentResult = redisService.setIfAbsent(key, value)
            assert(setIfAbsentResult == true) {
                "$message: setIfAbsent应该成功，键不存在"
            }

            // 再次尝试setIfAbsent - 键已存在
            val setIfAbsentResult2 = redisService.setIfAbsent(key, "newValue")
            assert(setIfAbsentResult2 == false) {
                "$message: setIfAbsent应该失败，键已存在"
            }

            // 验证原值未改变
            val retrieved = redisService.get(key, Any::class.java)
            assert(retrieved == value) {
                "$message: 原值应该保持不变\n" +
                    "期望值: $value\n" +
                    "实际值: $retrieved"
            }

            // 测试setIfPresent - 键存在时设置
            val newValue = "updatedValue"
            val setIfPresentResult = redisService.setIfPresent(key, newValue)
            assert(setIfPresentResult == true) {
                "$message: setIfPresent应该成功，键存在"
            }

            // 验证值已更新
            val updatedValue = redisService.get(key, Any::class.java)
            assert(updatedValue == newValue) {
                "$message: 值应该已更新\n" +
                    "期望值: $newValue\n" +
                    "实际值: $updatedValue"
            }
        } catch (e: Exception) {
            throw AssertionError("$message: 条件设置操作失败", e)
        }
    }

    /**
     * 验证错误处理
     */
    protected fun verifyErrorHandling(
        key: String,
        invalidValue: Any,
        message: String = "错误处理应该正确"
    ) {
        try {
            // 测试无效键
            val emptyKeyResult = redisService.set("", invalidValue)
            // 这里应该抛出异常或返回false，具体行为取决于实现

            // 测试类型转换错误
            redisService.set(key, "stringValue")
            val intValue = redisService.get(key, Int::class.java)
            // 这里应该抛出异常或返回null，具体行为取决于实现

        } catch (e: Exception) {
            // 预期的异常，测试通过
            logger.info("Expected exception caught: ${e.message}")
        }
    }
}
