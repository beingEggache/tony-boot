package com.tony.cache.test

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.tony.cache.RedisKeys
import com.tony.cache.RedisManager
import com.tony.enums.EnumCreator
import com.tony.enums.EnumIntValue
import com.tony.enums.EnumStringValue
import com.tony.exception.BizException
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisConnectionUtils
import javax.annotation.Resource

/**
 *
 * @author tangli
 * @since 2021-05-19 15:22
 */

@SpringBootTest(classes = [TestCacheApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RedisManagerTests {

    @Test
    fun testRedisValues() {
        val booleanKey = RedisKeys.genKey("value_test_boolean")
        val incrementKey = RedisKeys.genKey("value_test_increment")
        val intKey = RedisKeys.genKey("value_test_int")
        val longKey = RedisKeys.genKey("value_test_long")
        val doubleKey = RedisKeys.genKey("value_test_double")
        val stringKey = RedisKeys.genKey("value_test_string")
        val objKey = RedisKeys.genKey("value_test_obj")
        val intEnumKey = RedisKeys.genKey("value_test_int_enum")
        val stringEnumKey = RedisKeys.genKey("value_test_string_enum")

        RedisManager.values.set(booleanKey, false)
        RedisManager.values.increment(incrementKey, 66)
        RedisManager.values.set(intKey, 1)
        RedisManager.values.set(longKey, Long.MAX_VALUE)
        RedisManager.values.set(doubleKey, 1.012)
        RedisManager.values.set(stringKey, "test")
        RedisManager.values.set(objKey, Person("a", 20))
        RedisManager.values.set(intEnumKey, MyIntEnum.ONE)
        RedisManager.values.set(stringEnumKey, MyStringEnum.YES)

        val boolean = RedisManager.values.get<Boolean>(booleanKey)
        val increment = RedisManager.values.getInt(incrementKey)
        val int = RedisManager.values.getInt(intKey)
        val long = RedisManager.values.getLong(longKey)
        val double = RedisManager.values.getDouble(doubleKey)
        val string = RedisManager.values.getString(stringKey)
        val obj = RedisManager.values.getObj<Person>(objKey)
        val intEnum = RedisManager.values.getEnum<MyIntEnum, Int>(intEnumKey)
        val stringEnum = RedisManager.values.getEnum<MyStringEnum, String>(stringEnumKey)

        println(boolean)
        println(increment)
        println(int)
        println(long)
        println(double)
        println(string)
        println(obj)
        println(intEnum)
        println(stringEnum)
    }


    @Test
    fun testRedisMaps() {
        val testMapKey = RedisKeys.genKey("map")
        val testMapKey2 = RedisKeys.genKey("map2")
        val booleanKey = RedisKeys.genKey("value_test_boolean")
        val intKey = RedisKeys.genKey("value_test_int")
        val longKey = RedisKeys.genKey("value_test_long")
        val doubleKey = RedisKeys.genKey("value_test_double")
        val stringKey = RedisKeys.genKey("value_test_string")
        val objKey = RedisKeys.genKey("value_test_obj")
        val intEnumKey = RedisKeys.genKey("value_test_int_enum")
        val stringEnumKey = RedisKeys.genKey("value_test_string_enum")

        RedisManager.maps.put(testMapKey, booleanKey, false)
        RedisManager.maps.put(testMapKey, intKey, 1)
        RedisManager.maps.put(testMapKey, longKey, Long.MAX_VALUE)
        RedisManager.maps.put(testMapKey, doubleKey, 1.012)
        RedisManager.maps.put(testMapKey, stringKey, "test")
        RedisManager.maps.putObj(testMapKey, objKey, Person("a", 20))
        RedisManager.maps.put(testMapKey, intEnumKey, MyIntEnum.ONE)
        RedisManager.maps.put(testMapKey, stringEnumKey, MyStringEnum.YES)

        val boolean = RedisManager.maps.get<Boolean>(testMapKey, booleanKey)
        val int = RedisManager.maps.getInt(testMapKey, intKey)
        val long = RedisManager.maps.getLong(testMapKey, longKey)
        val double = RedisManager.maps.getDouble(testMapKey, doubleKey)
        val string = RedisManager.maps.getString(testMapKey, stringKey)
        val obj = RedisManager.maps.getObj<Person>(testMapKey, objKey)
        val intEnum = RedisManager.maps.getEnum<MyIntEnum, Int>(testMapKey, intEnumKey)
        val stringEnum = RedisManager.maps.getEnum<MyStringEnum, String>(testMapKey, stringEnumKey)
        val map = RedisManager.maps.getMap(testMapKey)

        RedisManager.maps.putAll(testMapKey2, map)

        val boolean2 = RedisManager.maps.get<Boolean>(testMapKey2, booleanKey)
        val int2 = RedisManager.maps.getInt(testMapKey2, intKey)
        val long2 = RedisManager.maps.getLong(testMapKey2, longKey)
        val double2 = RedisManager.maps.getDouble(testMapKey2, doubleKey)
        val string2 = RedisManager.maps.getString(testMapKey2, stringKey)
        val obj2 = RedisManager.maps.getObj<Person>(testMapKey2, objKey)
        val intEnum2 = RedisManager.maps.getEnum<MyIntEnum, Int>(testMapKey2, intEnumKey)
        val stringEnum2 = RedisManager.maps.getEnum<MyStringEnum, String>(testMapKey2, stringEnumKey)
        val map2 = RedisManager.maps.getMap(testMapKey2)

        println(boolean)
        println(int)
        println(long)
        println(double)
        println(string)
        println(obj)
        println(intEnum)
        println(stringEnum)
        println(map)

        println(boolean2)
        println(int2)
        println(long2)
        println(double2)
        println(string2)
        println(obj2)
        println(intEnum2)
        println(stringEnum2)
        println(map2)
    }

    @Test
    fun myEnumTests() {
        RedisManager.values.set(RedisKeys.genKey("value_test_my_int_enum"), MyIntEnum.ONE)
        RedisManager.values.set(RedisKeys.genKey("value_test_my_string_enum"), MyStringEnum.NO)

        val myIntEnum = RedisManager.values.getEnum<MyIntEnum, Int>(RedisKeys.genKey("value_test_my_int_enum"))
        val myStringEnum =
            RedisManager.values.getEnum<MyStringEnum, String>(RedisKeys.genKey("value_test_my_string_enum"))

        println(myIntEnum)
        println(myStringEnum)
    }


    @Test
    fun testString() {
        RedisConnectionUtils.bindConnection(RedisManager.redisTemplate.requiredConnectionFactory)
        RedisManager.redisTemplate.multi()
        RedisManager.redisTemplate.requiredConnectionFactory.connection.openPipeline()

        RedisConnectionUtils.unbindConnection(RedisManager.redisTemplate.requiredConnectionFactory)
    }

    @Test
    fun testMulti() {
        val redisTemplate = RedisManager.redisTemplate
        RedisConnectionUtils.bindConnection(redisTemplate.requiredConnectionFactory, true)
        RedisManager.redisTemplate.multi()
        RedisManager.values.set("a", "a")
        RedisManager.values.set("b", "b")
        RedisManager.values.set("c", "c")
        throw BizException("")
        redisTemplate.exec()
    }

}

class Person(val name: String, val age: Int)

enum class MyIntEnum(
    override val value: Int
) : EnumIntValue {

    @JsonEnumDefaultValue
    ZERO(0),
    ONE(1),
    ;

    companion object : EnumCreator<MyIntEnum, Int>(MyIntEnum::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}

enum class MyStringEnum(
    override val value: String
) : EnumStringValue {

    @JsonEnumDefaultValue
    YES("yes"),
    NO("NO"),
    ;

    companion object : EnumCreator<MyStringEnum, String>(MyStringEnum::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: String) =
            super.create(value)
    }
}
