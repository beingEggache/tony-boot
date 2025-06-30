package tony.test.redis

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import tony.redis.RedisManager
import tony.redis.RedisValues
import tony.test.redis.util.LargeTestObject
import tony.test.redis.util.ObjWithList
import tony.test.redis.util.ObjWithMap
import tony.test.redis.util.ObjWithNumberTypes
import tony.test.redis.util.OrderStatus
import tony.test.redis.util.SimpleObj
import tony.test.redis.util.TestIntEnum
import tony.test.redis.util.TestOrder
import tony.test.redis.util.TestStringEnum
import tony.test.redis.util.TestUser
import java.math.BigDecimal

/**
 * RedisValues 测试类
 *
 * 测试Redis值操作的各种功能，包括：
 * - 基本类型存储和获取
 * - 复杂对象序列化/反序列化
 * - 枚举类型转换
 * - 过期时间设置
 * - 条件设置操作
 *
 * @author tony
 * @since 2024-01-01
 */
@DisplayName("RedisValues 测试")
class RedisValuesTests : BaseRedisTest() {

    @Nested
    @DisplayName("基本类型操作")
    inner class BasicTypeOperations {

        @Test
        @DisplayName("字符串类型存储和获取")
        fun testStringOperations() {
            val key = generateTestKey("string")
            val value = "测试字符串"

            // 设置值
            RedisValues.set(key, value)

            // 获取值
            val retrieved = RedisValues.get<String>(key)
            assert(retrieved == value) { "字符串值不匹配" }

            // 验证数据转换
            verifySerializationDeserialization(value, String::class.java)
        }

        @Test
        @DisplayName("数字类型存储和获取")
        fun testNumberOperations() {
            val key = generateTestKey("number")
            val value = 12345L

            // 设置值
            RedisValues.set(key, value)

            // 获取值
            val retrieved = RedisValues.get<Long>(key)
            assert(retrieved == value) { "数字值不匹配" }

            // 验证数据转换
            verifySerializationDeserialization(value, Long::class.java)
        }

        @Test
        @DisplayName("布尔类型存储和获取")
        fun testBooleanOperations() {
            val key = generateTestKey("boolean")
            val value = true

            // 设置值
            RedisValues.set(key, value)

            // 获取值
            val retrieved = RedisValues.get<Boolean>(key)
            assert(retrieved == value) { "布尔值不匹配" }

            // 验证数据转换
            verifySerializationDeserialization(value, Boolean::class.java)
        }

        @Test
        @DisplayName("BigDecimal类型存储和获取")
        fun testBigDecimalOperations() {
            val key = generateTestKey("bigdecimal")
            val value = BigDecimal("123.456789")

            // 设置值
            RedisValues.set(key, value)

            // 获取值
            val retrieved = RedisValues.get<BigDecimal>(key)
            assert(retrieved == value) { "BigDecimal值不匹配" }

            // 验证数据转换
            verifySerializationDeserialization(value, BigDecimal::class.java)
        }
    }

    @Nested
    @DisplayName("复杂对象操作")
    inner class ComplexObjectOperations {

        @Test
        @DisplayName("简单对象存储和获取")
        fun testSimpleObjectOperations() {
            val key = generateTestKey("simple-obj")
            val value = getTestDataGenerator().createSimpleObj("测试对象", 25)
            RedisValues.set(key, value)
            val retrieved: SimpleObj? = RedisValues.get(key)
            assert(retrieved == value) { "简单对象不匹配" }
            verifyComplexObjectHandling(key, value, SimpleObj::class.java)
        }

        @Test
        @DisplayName("包含数字类型的对象存储和获取")
        fun testNumberTypesObjectOperations() {
            val key = generateTestKey("number-types-obj")
            val value = getTestDataGenerator().createObjWithNumberTypes()
            RedisValues.set(key, value)
            val retrieved: ObjWithNumberTypes? = RedisValues.get(key)
            assert(retrieved == value) { "数字类型对象不匹配" }
            verifyComplexObjectHandling(key, value, ObjWithNumberTypes::class.java)
        }

        @Test
        @DisplayName("包含列表的对象存储和获取")
        fun testListObjectOperations() {
            val key = generateTestKey("list-obj")
            val value = getTestDataGenerator().createObjWithList("列表对象", listOf("item1", "item2", "item3"))
            RedisValues.set(key, value)
            val retrieved: ObjWithList? = RedisValues.get(key)
            assert(retrieved == value) { "列表对象不匹配" }
            verifyComplexObjectHandling(key, value, ObjWithList::class.java)
        }

        @Test
        @DisplayName("包含Map的对象存储和获取")
        fun testMapObjectOperations() {
            val key = generateTestKey("map-obj")
            val value = getTestDataGenerator().createObjWithMap("Map对象")
            RedisValues.set(key, value)
            val retrieved: ObjWithMap? = RedisValues.get(key)
            assert(retrieved == value) { "Map对象不匹配" }
            verifyComplexObjectHandling(key, value, ObjWithMap::class.java)
        }

        @Test
        @DisplayName("大对象存储和获取")
        fun testLargeObjectOperations() {
            val key = generateTestKey("large-obj")
            val value = getTestDataGenerator().createLargeObject()
            RedisValues.set(key, value)
            val retrieved: LargeTestObject? = RedisValues.get(key)
            assert(retrieved == value) { "大对象不匹配" }
            verifyComplexObjectHandling(key, value, LargeTestObject::class.java)
        }
    }

    @Nested
    @DisplayName("枚举类型操作")
    inner class EnumOperations {

        @Test
        @DisplayName("字符串枚举存储和获取")
        fun testStringEnumOperations() {
            val key = generateTestKey("string-enum")
            val value = TestStringEnum.YES

            // 设置值
            RedisValues.set(key, value)

            // 获取值
            val retrieved = RedisValues.get<TestStringEnum>(key)
            assert(retrieved == value) { "字符串枚举不匹配" }

            // 验证枚举处理
            verifyStringEnumHandling(key, value, TestStringEnum::class.java)
        }

        @Test
        @DisplayName("整数枚举存储和获取")
        fun testIntEnumOperations() {
            val key = generateTestKey("int-enum")
            val value = TestIntEnum.ACTIVE

            // 设置值
            RedisValues.set(key, value)

            // 获取值
            val retrieved = RedisValues.get<TestIntEnum>(key)
            assert(retrieved == value) { "整数枚举不匹配" }

            // 验证枚举处理
            verifyIntEnumHandling(key, value, TestIntEnum::class.java)
        }

        @Test
        @DisplayName("包含枚举的对象存储和获取")
        fun testObjectWithEnumOperations() {
            val key = generateTestKey("obj-with-enum")
            val value = getTestDataGenerator().createTestOrder(
                orderId = "ORDER123",
                amount = BigDecimal("99.99"),
                status = OrderStatus.PENDING
            )

            // 设置值
            RedisValues.set(key, value)

            // 获取值
            val retrieved = RedisValues.get<TestOrder>(key)
            assert(retrieved == value) { "包含枚举的对象不匹配" }

            // 验证复杂对象处理
            verifyComplexObjectHandling(key, value, TestOrder::class.java)
        }
    }

    @Nested
    @DisplayName("过期时间操作")
    inner class ExpirationOperations {

        @Test
        @DisplayName("设置带过期时间的值")
        fun testSetWithExpiration() {
            val key = generateTestKey("expiration")
            val value = "带过期时间的值"
            val expirationSeconds = 2L

            // 设置带过期时间的值
            RedisValues.set(key, value, expirationSeconds)

            // 立即获取值
            val retrieved = RedisValues.get<String>(key)
            assert(retrieved == value) { "设置后立即获取值不正确" }

            // 等待过期时间后获取值
            Thread.sleep((expirationSeconds + 1) * 1000)
            val expiredValue = RedisValues.get<String>(key)
            assert(expiredValue == null) { "过期后值应该为null" }
        }

        @Test
        @DisplayName("设置带过期时间的复杂对象")
        fun testSetComplexObjectWithExpiration() {
            val key = generateTestKey("complex-expiration")
            val value = getTestDataGenerator().createTestUser(
                id = 1L,
                name = "过期用户",
                age = 30
            )
            val expirationSeconds = 2L

            // 设置带过期时间的复杂对象
            RedisValues.set(key, value, expirationSeconds)

            // 立即获取值
            val retrieved = RedisValues.get<TestUser>(key)
            assert(retrieved == value) { "设置后立即获取复杂对象不正确" }

            // 等待过期时间后获取值
            Thread.sleep((expirationSeconds + 1) * 1000)
            val expiredValue = RedisValues.get<TestUser>(key)
            assert(expiredValue == null) { "过期后复杂对象应该为null" }
        }
    }

    @Nested
    @DisplayName("条件设置操作")
    inner class ConditionalOperations {

        @Test
        @DisplayName("setIfAbsent - 键不存在时设置")
        fun testSetIfAbsent() {
            val key = generateTestKey("set-if-absent")
            val value = "新值"

            // 测试setIfAbsent - 键不存在时设置
            val result = RedisValues.setIfAbsent(key, value)
            assert(result == true) { "setIfAbsent应该成功，键不存在" }

            // 验证值已设置
            val retrieved = RedisValues.get<String>(key)
            assert(retrieved == value) { "setIfAbsent后值应该正确设置" }

            // 再次尝试setIfAbsent - 键已存在
            val result2 = RedisValues.setIfAbsent(key, "另一个值")
            assert(result2 == false) { "setIfAbsent应该失败，键已存在" }

            // 验证原值未改变
            val retrieved2 = RedisValues.get<String>(key)
            assert(retrieved2 == value) { "原值应该保持不变" }
        }

        @Test
        @DisplayName("setIfPresent - 键存在时设置")
        fun testSetIfPresent() {
            val key = generateTestKey("set-if-present")
            val originalValue = "原始值"
            val newValue = "新值"

            // 先设置一个值
            RedisValues.set(key, originalValue)

            // 测试setIfPresent - 键存在时设置
            val result = RedisValues.setIfPresent(key, newValue)
            assert(result == true) { "setIfPresent应该成功，键存在" }

            // 验证值已更新
            val retrieved = RedisValues.get<String>(key)
            assert(retrieved == newValue) { "setIfPresent后值应该已更新" }

            // 删除键
            RedisManager.delete(key)

            // 测试setIfPresent - 键不存在时设置
            val result2 = RedisValues.setIfPresent(key, "另一个值")
            assert(result2 == false) { "setIfPresent应该失败，键不存在" }
        }
    }

    @Nested
    @DisplayName("批量操作")
    inner class BatchOperations {

        @Test
        @DisplayName("批量设置和获取")
        fun testBatchOperations() {
            val keys = generateTestKeys(3, "batch")
            val values = listOf("值1", "值2", "值3")

            // 批量设置
            keys.forEachIndexed { index, key ->
                RedisValues.set(key, values[index])
            }

            // 批量获取
            keys.forEachIndexed { index, key ->
                val retrieved = RedisValues.get<String>(key)
                assert(retrieved == values[index]) { "批量操作中键 $key 的值不正确" }
            }
        }

        @Test
        @DisplayName("批量删除")
        fun testBatchDelete() {
            val keys = generateTestKeys(3, "batch-delete")

            // 设置值
            keys.forEach { key ->
                RedisValues.set(key, "测试值")
            }

            // 验证值存在
            keys.forEach { key ->
                val value = RedisValues.get<String>(key)
                assert(value != null) { "键 $key 的值应该存在" }
            }

            // 批量删除
            keys.forEach { key ->
                RedisManager.delete(key)
            }

            // 验证值已删除
            keys.forEach { key ->
                val value = RedisValues.get<String>(key)
                assert(value == null) { "键 $key 的值应该已删除" }
            }
        }
    }

    @Nested
    @DisplayName("特殊字符和边界情况")
    inner class SpecialCharactersAndEdgeCases {

        @Test
        @DisplayName("特殊字符处理")
        fun testSpecialCharacterHandling() {
            val key = generateTestKey("special-chars")
            val value = """特殊字符: !@#$%^&*()_+-=[]{}|;':",./<>?"""

            // 设置包含特殊字符的值
            RedisValues.set(key, value)

            // 获取值
            val retrieved = RedisValues.get<String>(key)

            // 验证序列化反序列化一致性（允许JSON转义）
            assert(retrieved != null) { "特殊字符处理失败，获取值为null" }

            // 验证序列化反序列化一致性
            verifySerializationDeserialization(value, String::class.java, "特殊字符序列化反序列化应该一致")

            // 验证获取的值经过反序列化后与原始值相等
            val finalValue = retrieved
            assert(finalValue == value) {
                "特殊字符处理不正确\n" +
                "原始值: '$value'\n" +
                "获取值: '$finalValue'\n" +
                "长度比较: 原始=${value.length}, 获取=${finalValue?.length}"
            }
        }

        @Test
        @DisplayName("中文字符处理")
        fun testChineseCharacterHandling() {
            val key = generateTestKey("chinese-chars")
            val value = "中文字符：你好世界！"

            // 设置包含中文字符的值
            RedisValues.set(key, value)

            // 获取值
            val retrieved = RedisValues.get<String>(key)
            assert(retrieved == value) { "中文字符处理不正确" }
        }

        @Test
        @DisplayName("空字符串处理")
        fun testEmptyStringHandling() {
            val key = generateTestKey("empty-string")
            val value = ""

            // 设置空字符串
            RedisValues.set(key, value)

            // 获取值
            val retrieved = RedisValues.get<String>(key)
            assert(retrieved == value) { "空字符串处理不正确" }
        }

        @Test
        @DisplayName("不存在的键获取")
        fun testNonExistentKeyGet() {
            val key = generateTestKey("non-existent")

            // 获取不存在的键
            val retrieved = RedisValues.get<String>(key)
            assert(retrieved == null) { "不存在的键应该返回null" }
        }
    }

    @Nested
    @DisplayName("错误处理")
    inner class ErrorHandling {

        @Test
        @DisplayName("类型转换错误处理")
        fun testTypeConversionErrorHandling() {
            val key = generateTestKey("type-conversion")
            val stringValue = "字符串值"

            // 设置字符串值
            RedisValues.set(key, stringValue)
            // 这里应该返回null或抛出异常，具体行为取决于实现
            Assertions.assertThrows(NumberFormatException::class.java) {
                // 尝试获取为数字类型
                RedisValues.get<Int>(key)
            }
        }
    }
}
