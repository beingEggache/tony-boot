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

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import tony.core.utils.asTo
import tony.core.utils.toJsonString
import tony.redis.RedisManager
import tony.redis.RedisMaps
import tony.test.redis.util.ObjWithList
import tony.test.redis.util.ObjWithMap
import tony.test.redis.util.ObjWithNumberTypes
import tony.test.redis.util.SimpleObj
import tony.test.redis.util.TestIntEnum
import tony.test.redis.util.TestStringEnum

/**
 * RedisMaps 测试类
 *
 * 测试Redis哈希表操作的各种功能，包括：
 * - 基本类型存储和获取
 * - 复杂对象序列化/反序列化
 * - 枚举类型转换
 * - 过期时间设置
 * - 条件设置操作
 *
 * @author tony
 * @date 2025/07/01 17:00
 */
@DisplayName("RedisMaps 测试")
class RedisMapsTests : BaseRedisTest() {

    @Nested
    @DisplayName("Map边界与特殊场景")
    inner class MapEdgeCaseOperations {
        @Test
        @DisplayName("空Map存取")
        fun testEmptyMap() {
            val key = generateTestKey("map-empty")
            val emptyMap = emptyMap<String, Any?>()
            RedisMaps.putAll(key, emptyMap)
            val retrieved = RedisMaps.entries(key)
            Assertions.assertTrue(retrieved.isEmpty(), "空Map应返回空")
        }

        @Test
        @DisplayName("嵌套Map存取")
        fun testNestedMap() {
            val key = generateTestKey("map-nested")
            val nested = mapOf("f1" to mapOf("sub1" to 1, "sub2" to 2), "f2" to mapOf("a" to "A"))
            RedisMaps.putAll(key, nested)
            val retrieved = RedisMaps.entries(key)
            Assertions.assertEquals(nested.size, retrieved.size)
            Assertions.assertTrue(retrieved["f1"] is Map<*, *>)
            Assertions.assertTrue(retrieved["f2"] is Map<*, *>)
        }

        @Test
        @DisplayName("Map value为null")
        fun testMapValueNull() {
            val key = generateTestKey("map-null-value")
            val data = mapOf("f1" to null, "f2" to "v2")
            RedisMaps.putAll(key, data)
            val retrieved = RedisMaps.entries(key)
            Assertions.assertTrue(retrieved.containsKey("f1"))
            Assertions.assertNull(retrieved["f1"])
            Assertions.assertEquals("v2", retrieved["f2"])
        }

        @Test
        @DisplayName("Map批量删除、批量获取部分字段、字段覆盖")
        fun testBatchDeleteAndFieldOverwrite() {
            val key = generateTestKey("map-batch-del-overwrite")
            val data1 = mapOf("f1" to 1, "f2" to 2, "f3" to 3)
            RedisMaps.putAll(key, data1)
            val data2 = mapOf("f2" to 22, "f4" to 4)
            RedisMaps.putAll(key, data2) // f2覆盖，f4新增
            val all = RedisMaps.entries(key)
            Assertions.assertEquals(4, all.size)
            Assertions.assertEquals(1, all["f1"])
            Assertions.assertEquals(22, all["f2"])
            Assertions.assertEquals(3, all["f3"])
            Assertions.assertEquals(4, all["f4"])
            // 批量删除
            RedisMaps.delete(key, "f1", "f3")
            val afterDel = RedisMaps.entries(key)
            Assertions.assertFalse(afterDel.containsKey("f1"))
            Assertions.assertFalse(afterDel.containsKey("f3"))
            Assertions.assertEquals(2, afterDel.size)
        }
    }

    @Nested
    @DisplayName("异常与类型不匹配场景")
    inner class MapTypeErrorOperations {
        @Test
        @DisplayName("类型不匹配异常")
        fun testTypeMismatch() {
            val key = generateTestKey("map-type-mismatch")
            RedisMaps.put(key, "f", "string")
            Assertions.assertThrows(Exception::class.java) {
                RedisMaps.get<Int>(key, "f")
            }
        }
    }

    @Nested
    @DisplayName("特殊字符与序列化边界")
    inner class MapSpecialCharOperations {
        @Test
        @DisplayName("Map的key/value包含特殊字符")
        fun testSpecialCharKeyValue() {
            val key = generateTestKey("map-special-char")
            val data = mapOf(
                "空格 key" to "value with 空格",
                "换行\nkey" to "tab\tvalue",
                "引号\"key" to "单引号'value",
                "emoji😊" to "特殊字符!@#￥%……&*()"
            )
            RedisMaps.putAll(key, data)
            val retrieved = RedisMaps.entries(key)
            Assertions.assertEquals(data, retrieved)
        }

        @Test
        @DisplayName("超长字符串和极端数字")
        fun testLongStringAndExtremeNumber() {
            val key = generateTestKey("map-long-extreme")
            val longStr = "a".repeat(10000)
            val data = mapOf(
                "long" to longStr,
                "max" to Long.MAX_VALUE,
                "min" to Long.MIN_VALUE
            )
            RedisMaps.putAll(key, data)
            val retrieved = RedisMaps.entries(key)
            Assertions.assertEquals(longStr, retrieved["long"])
            Assertions.assertEquals(Long.MAX_VALUE, retrieved["max"])
            Assertions.assertEquals(Long.MIN_VALUE, retrieved["min"])
        }
    }

    @Nested
    @DisplayName("基本类型操作")
    inner class BasicTypeOperations {
        @Test
        @DisplayName("字符串类型存储和获取")
        fun testStringOperations() {
            val key = generateTestKey("map-string")
            val hashKey = "f1"
            val value = "测试字符串"
            RedisMaps.put(key, hashKey, value)
            val retrieved = RedisMaps.get<String>(key, hashKey)
            Assertions.assertEquals(value, retrieved, "字符串值不匹配")
        }

        @Test
        @DisplayName("数字类型存储和获取")
        fun testNumberOperations() {
            val key = generateTestKey("map-number")
            val hashKey = "f2"
            val value = 12345L
            RedisMaps.put(key, hashKey, value)
            val retrieved = RedisMaps.get<Long>(key, hashKey)
            Assertions.assertEquals(value, retrieved, "数字值不匹配")
        }

        @Test
        @DisplayName("布尔类型存储和获取")
        fun testBooleanOperations() {
            val key = generateTestKey("map-boolean")
            val hashKey = "f3"
            val value = true
            RedisMaps.put(key, hashKey, value)
            val retrieved = RedisMaps.get<Boolean>(key, hashKey)
            Assertions.assertEquals(value, retrieved, "布尔值不匹配")
        }
    }

    @Nested
    @DisplayName("复杂对象操作")
    inner class ComplexObjectOperations {
        @Test
        @DisplayName("简单对象存储和获取")
        fun testSimpleObjectOperations() {
            val key = generateTestKey("map-simple-obj")
            val hashKey = "obj"
            val value = getTestDataGenerator().createSimpleObj("测试对象", 25)
            RedisMaps.put(key, hashKey, value)
            val retrieved = RedisMaps.get<SimpleObj>(key, hashKey)
            Assertions.assertEquals(value, retrieved, "简单对象不匹配")
        }

        @Test
        @DisplayName("包含数字类型的对象存储和获取")
        fun testNumberTypesObjectOperations() {
            val key = generateTestKey("map-number-types-obj")
            val hashKey = "obj"
            val value = getTestDataGenerator().createObjWithNumberTypes()
            RedisMaps.put(key, hashKey, value)
            val retrieved = RedisMaps.get<ObjWithNumberTypes>(key, hashKey)
            Assertions.assertEquals(value, retrieved, "数字类型对象不匹配")
        }

        @Test
        @DisplayName("包含列表的对象存储和获取")
        fun testListObjectOperations() {
            val key = generateTestKey("map-list-obj")
            val hashKey = "obj"
            val value = getTestDataGenerator().createObjWithList("列表对象", listOf("item1", "item2", "item3"))
            RedisMaps.put(key, hashKey, value)
            val retrieved = RedisMaps.get<ObjWithList>(key, hashKey)
            Assertions.assertEquals(value, retrieved, "列表对象不匹配")
        }

        @Test
        @DisplayName("包含Map的对象存储和获取")
        fun testMapObjectOperations() {
            val key = generateTestKey("map-map-obj")
            val hashKey = "obj"
            val value = getTestDataGenerator().createObjWithMap("Map对象")
            RedisMaps.put(key, hashKey, value)
            val retrieved = RedisMaps.get<ObjWithMap>(key, hashKey)
            Assertions.assertEquals(value, retrieved, "Map对象不匹配")
        }
    }

    @Nested
    @DisplayName("枚举类型操作")
    inner class EnumOperations {
        @Test
        @DisplayName("字符串枚举存储和获取")
        fun testStringEnumOperations() {
            val key = generateTestKey("map-string-enum")
            val hashKey = "enum"
            val value = TestStringEnum.YES
            RedisMaps.put(key, hashKey, value)
            val retrieved = RedisMaps.get<TestStringEnum>(key, hashKey)
            Assertions.assertEquals(value, retrieved, "字符串枚举不匹配")
        }

        @Test
        @DisplayName("整数枚举存储和获取")
        fun testIntEnumOperations() {
            val key = generateTestKey("map-int-enum")
            val hashKey = "enum"
            val value = TestIntEnum.ACTIVE
            RedisMaps.put(key, hashKey, value)
            val retrieved = RedisMaps.get<TestIntEnum>(key, hashKey)
            Assertions.assertEquals(value, retrieved, "整数枚举不匹配")
        }
    }

    @Nested
    @DisplayName("过期时间与条件操作")
    inner class ExpireAndConditionOperations {
        @Test
        @DisplayName("设置过期时间")
        fun testExpire() {
            val key = generateTestKey("map-expire")
            val hashKey = "f"
            RedisMaps.put(key, hashKey, "v")
            RedisManager.expire(key, 1)
            Thread.sleep(1100)
            val retrieved = RedisMaps.get<String>(key, hashKey)
            Assertions.assertNull(retrieved, "过期后应为null")
        }

        @Test
        @DisplayName("仅在字段不存在时设置")
        fun testSetIfAbsent() {
            val key = generateTestKey("map-set-if-absent")
            val hashKey = "f"
            val value1 = "v1"
            val value2 = "v2"
            val first = RedisMaps.putIfAbsent(key, hashKey, value1)
            val second = RedisMaps.putIfAbsent(key, hashKey, value2)
            Assertions.assertTrue(first, "第一次应设置成功")
            Assertions.assertFalse(second, "第二次应设置失败")
            val retrieved = RedisMaps.get<String>(key, hashKey)
            Assertions.assertEquals(value1, retrieved, "值应为第一次设置的")
        }
    }

    @Nested
    @DisplayName("批量操作与边界场景")
    inner class BatchAndEdgeOperations {
        @Test
        @DisplayName("批量设置和获取")
        fun testBatchSetAndGet() {
            val key = generateTestKey("map-batch")
            val data = mapOf(
                "f1" to "v1",
                "f2" to 123,
                "f3" to true
            )
            RedisMaps.putAll(key, data)
            val all = RedisMaps.entries(key)
            Assertions.assertEquals(data.size, all.size)
            Assertions.assertEquals("v1", all["f1"])
            Assertions.assertEquals(123, all["f2"])
            Assertions.assertEquals(true, all["f3"])
        }

        @Test
        @DisplayName("删除字段")
        fun testDeleteField() {
            val key = generateTestKey("map-delete")
            val hashKey = "f"
            RedisMaps.put(key, hashKey, "v")
            RedisMaps.delete(key, hashKey)
            val retrieved = RedisMaps.get<String>(key, hashKey)
            Assertions.assertNull(retrieved, "删除后应为null")
        }
    }

    @Nested
    @DisplayName("Map value为复杂对象")
    inner class MapComplexValueOperations {
        @Test
        @DisplayName("SimpleObj为value")
        fun testSimpleObjValue() {
            val key = generateTestKey("map-complex-simpleobj")
            val obj = getTestDataGenerator().createSimpleObj("复杂对象", 99)
            val data = mapOf("f1" to obj)
            RedisMaps.putAll(key, data)
            val retrieved = RedisMaps.entries(key)
            val mapValue = retrieved["f1"].asTo<Map<String, *>>()
            Assertions.assertNotNull(mapValue)
            Assertions.assertEquals(obj.age, mapValue!!["age"])
            Assertions.assertEquals(obj.name, mapValue["name"])
        }

        @Test
        @DisplayName("ObjWithList为value")
        fun testObjWithListValue() {
            val key = generateTestKey("map-complex-listobj")
            val obj = getTestDataGenerator().createObjWithList("列表对象", listOf("a", "b"))
            val data = mapOf("f1" to obj)
            RedisMaps.putAll(key, data)
            val retrieved = RedisMaps.entries(key)
            val mapValue = retrieved["f1"].asTo<Map<String, *>>()
            Assertions.assertNotNull(mapValue)
            Assertions.assertEquals(obj.name, mapValue!!["name"])
            Assertions.assertEquals(obj.list, mapValue["list"])
        }

        @Test
        @DisplayName("ObjWithMap为value")
        fun testObjWithMapValue() {
            val key = generateTestKey("map-complex-mapobj")
            val obj = getTestDataGenerator().createObjWithMap("Map对象")
            val data = mapOf("f1" to obj)
            RedisMaps.putAll(key, data)
            val retrieved = RedisMaps.entries(key)
            val mapValue = retrieved["f1"].asTo<Map<String, *>>()
            Assertions.assertNotNull(mapValue)
            Assertions.assertEquals(obj.name, mapValue!!["name"])
            Assertions.assertEquals(obj.map.toJsonString(), mapValue["map"].toJsonString())
        }
    }
}
