package com.tony.cache.test

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.tony.cache.RedisKeys
import com.tony.cache.RedisUtils
import com.tony.enums.EnumCreator
import com.tony.enums.EnumIntValue
import com.tony.enums.EnumStringValue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 *
 * @author tangli
 * @since 2021-05-19 15:22
 */

@SpringBootTest(classes = [TestCacheApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RedisUtilsTests {

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

        RedisUtils.values.set(booleanKey, false)
        RedisUtils.values.increment(incrementKey, 66)
        RedisUtils.values.setNumber(intKey, 1)
        RedisUtils.values.setNumber(longKey, Long.MAX_VALUE)
        RedisUtils.values.setNumber(doubleKey, 1.012)
        RedisUtils.values.setString(stringKey, "test")
        RedisUtils.values.setObj(objKey, Person("a", 20))
        RedisUtils.values.set(intEnumKey, MyIntEnum.ONE)
        RedisUtils.values.set(stringEnumKey, MyStringEnum.YES)

        val boolean = RedisUtils.values.get<Boolean>(booleanKey)
        val increment = RedisUtils.values.getInt(incrementKey)
        val int = RedisUtils.values.getInt(intKey)
        val long = RedisUtils.values.getLong(longKey)
        val double = RedisUtils.values.getDouble(doubleKey)
        val string = RedisUtils.values.getString(stringKey)
        val obj = RedisUtils.values.getObj<Person>(objKey)
        val intEnum = RedisUtils.values.getEnum<MyIntEnum, Int>(intEnumKey)
        val stringEnum = RedisUtils.values.getEnum<MyStringEnum, String>(stringEnumKey)

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

        RedisUtils.maps.put(testMapKey, booleanKey, false)
        RedisUtils.maps.putNumber(testMapKey, intKey, 1)
        RedisUtils.maps.putNumber(testMapKey, longKey, Long.MAX_VALUE)
        RedisUtils.maps.putNumber(testMapKey, doubleKey, 1.012)
        RedisUtils.maps.putString(testMapKey, stringKey, "test")
        RedisUtils.maps.putObj(testMapKey, objKey, Person("a", 20))
        RedisUtils.maps.put(testMapKey, intEnumKey, MyIntEnum.ONE)
        RedisUtils.maps.put(testMapKey, stringEnumKey, MyStringEnum.YES)

        val boolean = RedisUtils.maps.get<Boolean>(testMapKey, booleanKey)
        val int = RedisUtils.maps.getInt(testMapKey, intKey)
        val long = RedisUtils.maps.getLong(testMapKey, longKey)
        val double = RedisUtils.maps.getDouble(testMapKey, doubleKey)
        val string = RedisUtils.maps.getString(testMapKey, stringKey)
        val obj = RedisUtils.maps.getObj<Person>(testMapKey, objKey)
        val intEnum = RedisUtils.maps.getEnum<MyIntEnum, Int>(testMapKey, intEnumKey)
        val stringEnum = RedisUtils.maps.getEnum<MyStringEnum, String>(testMapKey, stringEnumKey)
        val map = RedisUtils.maps.getMap(testMapKey)

        RedisUtils.maps.putAll(testMapKey2, map)

        val boolean2 = RedisUtils.maps.get<Boolean>(testMapKey2, booleanKey)
        val int2 = RedisUtils.maps.getInt(testMapKey2, intKey)
        val long2 = RedisUtils.maps.getLong(testMapKey2, longKey)
        val double2 = RedisUtils.maps.getDouble(testMapKey2, doubleKey)
        val string2 = RedisUtils.maps.getString(testMapKey2, stringKey)
        val obj2 = RedisUtils.maps.getObj<Person>(testMapKey2, objKey)
        val intEnum2 = RedisUtils.maps.getEnum<MyIntEnum, Int>(testMapKey2, intEnumKey)
        val stringEnum2 = RedisUtils.maps.getEnum<MyStringEnum, String>(testMapKey2, stringEnumKey)
        val map2 = RedisUtils.maps.getMap(testMapKey2)

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
        RedisUtils.values.set(RedisKeys.genKey("value_test_my_int_enum"), MyIntEnum.ONE)
        RedisUtils.values.set(RedisKeys.genKey("value_test_my_string_enum"), MyStringEnum.NO)

        val myIntEnum = RedisUtils.values.getEnum<MyIntEnum, Int>(RedisKeys.genKey("value_test_my_int_enum"))
        val myStringEnum = RedisUtils.values.getEnum<MyStringEnum, String>(RedisKeys.genKey("value_test_my_string_enum"))

        println(myIntEnum)
        println(myStringEnum)
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
