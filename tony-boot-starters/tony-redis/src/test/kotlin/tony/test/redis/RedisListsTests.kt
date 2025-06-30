package tony.test.redis

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import tony.redis.RedisLists
import tony.test.redis.util.ObjWithList
import tony.test.redis.util.SimpleObj
import tony.test.redis.util.TestIntEnum
import tony.test.redis.util.TestStringEnum

/**
 * RedisLists 测试类
 *
 * 测试Redis列表操作的各种功能，包括：
 * - 基本类型存取
 * - 复杂对象/枚举类型存取
 * - 批量/条件操作
 * - 边界/异常/裁剪/转移等
 *
 * @author tony
 * @since 2025-07-01
 */
@DisplayName("RedisLists 测试")
class RedisListsTests : BaseRedisTest() {

    @Nested
    @DisplayName("基本类型操作")
    inner class BasicTypeOperations {
        @Test
        @DisplayName("字符串类型存取")
        fun testStringList() {
            val key = generateTestKey("list-string")
            RedisLists.leftPush(key, "a")
            RedisLists.rightPush(key, "b")
            val left = RedisLists.leftPop<String>(key)
            val right = RedisLists.rightPop<String>(key)
            Assertions.assertEquals("a", left)
            Assertions.assertEquals("b", right)
        }

        @Test
        @DisplayName("数字类型存取")
        fun testNumberList() {
            val key = generateTestKey("list-number")
            RedisLists.rightPush(key, 1)
            RedisLists.rightPush(key, 2)
            val v1 = RedisLists.leftPop<Int>(key)
            val v2 = RedisLists.leftPop<Int>(key)
            Assertions.assertEquals(1, v1)
            Assertions.assertEquals(2, v2)
        }

        @Test
        @DisplayName("布尔类型存取")
        fun testBooleanList() {
            val key = generateTestKey("list-bool")
            RedisLists.leftPush(key, true)
            RedisLists.leftPush(key, false)
            val v1 = RedisLists.rightPop<Boolean>(key)
            val v2 = RedisLists.rightPop<Boolean>(key)
            Assertions.assertEquals(true, v1)
            Assertions.assertEquals(false, v2)
        }
    }

    @Nested
    @DisplayName("复杂对象操作")
    inner class ComplexObjectOperations {
        @Test
        @DisplayName("SimpleObj存取")
        fun testSimpleObjList() {
            val key = generateTestKey("list-simpleobj")
            val obj = getTestDataGenerator().createSimpleObj("对象", 1)
            RedisLists.leftPush(key, obj)
            val pop = RedisLists.leftPop<SimpleObj>(key)
            Assertions.assertNotNull(pop)
            Assertions.assertEquals(obj.name, pop?.name)
            Assertions.assertEquals(obj.age, pop?.age)
        }

        @Test
        @DisplayName("ObjWithList存取")
        fun testObjWithListList() {
            val key = generateTestKey("list-listobj")
            val obj = getTestDataGenerator().createObjWithList("列表", listOf("x", "y"))
            RedisLists.rightPush(key, obj)
            val pop = RedisLists.rightPop<ObjWithList>(key)
            Assertions.assertNotNull(pop)
            Assertions.assertEquals(obj.name, pop?.name)
            Assertions.assertEquals(obj.list, pop?.list)
        }
    }

    @Nested
    @DisplayName("枚举类型操作")
    inner class EnumTypeOperations {
        @Test
        @DisplayName("TestStringEnum存取")
        fun testStringEnumList() {
            val key = generateTestKey("list-enum-string")
            RedisLists.leftPush(key, TestStringEnum.YES)
            val pop = RedisLists.leftPop<Any>(key)
            Assertions.assertEquals(TestStringEnum.YES.value, pop)
        }

        @Test
        @DisplayName("TestIntEnum存取")
        fun testIntEnumList() {
            val key = generateTestKey("list-enum-int")
            RedisLists.rightPush(key, TestIntEnum.ACTIVE)
            val pop = RedisLists.rightPop<TestIntEnum>(key)
            Assertions.assertEquals(TestIntEnum.ACTIVE.value, pop?.value)
        }
    }

    @Nested
    @DisplayName("批量与条件操作")
    inner class BatchAndConditionOperations {
        @Test
        @DisplayName("批量leftPushAll/rightPushAll")
        fun testBatchPushAll() {
            val key = generateTestKey("list-batch")
            val list = listOf("a", "b", "c")
            RedisLists.leftPushAll(key, list)
            val range = RedisLists.range<String>(key, 0, -1)
            Assertions.assertEquals(list.reversed(), range)
        }

        @Test
        @DisplayName("leftPushIfPresent/rightPushIfPresent")
        fun testPushIfPresent() {
            val key = generateTestKey("list-present")
            val absent = RedisLists.leftPushIfPresent(key, "x")
            Assertions.assertEquals(0, absent)
            RedisLists.leftPush(key, "a")
            val present = RedisLists.rightPushIfPresent(key, "b")
            Assertions.assertEquals(2, present)
            val range = RedisLists.range<String>(key, 0, -1)
            Assertions.assertEquals(listOf("a", "b"), range)
        }
    }

    @Nested
    @DisplayName("边界与异常场景")
    inner class EdgeAndErrorOperations {
        @Test
        @DisplayName("空列表弹出")
        fun testPopEmpty() {
            val key = generateTestKey("list-empty")
            val left = RedisLists.leftPop<String>(key)
            val right = RedisLists.rightPop<String>(key)
            Assertions.assertNull(left)
            Assertions.assertNull(right)
        }

        @Test
        @DisplayName("索引越界")
        fun testIndexOutOfBounds() {
            val key = generateTestKey("list-index")
            RedisLists.leftPush(key, "a")
            val v = RedisLists.index<String>(key, 10)
            Assertions.assertNull(v)
        }

        @Test
        @DisplayName("类型不匹配异常")
        fun testTypeMismatch() {
            val key = generateTestKey("list-type-mismatch")
            RedisLists.leftPush(key, "string")
            Assertions.assertThrows(Exception::class.java) {
                RedisLists.leftPop<Int>(key)
            }
        }
    }

    @Nested
    @DisplayName("裁剪与转移操作")
    inner class TrimAndTransferOperations {
        @Test
        @DisplayName("trim裁剪")
        fun testTrim() {
            val key = generateTestKey("list-trim")
            RedisLists.rightPushAll(key, listOf(1, 2, 3, 4, 5))
            RedisLists.trim(key, 1, 3)
            val range = RedisLists.range<Int>(key, 0, -1)
            Assertions.assertEquals(listOf(2, 3, 4), range)
        }

        @Test
        @DisplayName("rightPopAndLeftPush转移")
        fun testRightPopAndLeftPush() {
            val key1 = generateTestKey("list-transfer1")
            val key2 = generateTestKey("list-transfer2")
            RedisLists.rightPushAll(key1, listOf("a", "b"))
            val v = RedisLists.rightPopAndLeftPush(key1, key2, String::class.java)
            Assertions.assertEquals("b", v)
            val left = RedisLists.leftPop<String>(key2)
            Assertions.assertEquals("b", left)
        }
    }
}
