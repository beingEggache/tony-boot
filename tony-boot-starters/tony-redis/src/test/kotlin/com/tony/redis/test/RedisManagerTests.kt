package com.tony.redis.test

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.tony.enums.EnumCreator
import com.tony.enums.EnumIntValue
import com.tony.enums.EnumStringValue
import com.tony.enums.IntEnumCreator
import com.tony.enums.StringEnumCreator
import com.tony.exception.BizException
import com.tony.redis.RedisKeys
import com.tony.redis.RedisManager
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.RepetitionInfo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertNotNull

/**
 *
 * @author tangli
 * @since 2021-05-19 15:22
 */

@SpringBootTest(classes = [TestRedisApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RedisManagerTests {

    private val logger = LoggerFactory.getLogger(RedisManagerTests::class.java)

    @Execution(ExecutionMode.CONCURRENT)
    @Test
    fun testRedisValues(testInfo: TestInfo) {
        val keyPrefix = testInfo.testMethod.get().name

        val booleanKey = RedisKeys.genKey("$keyPrefix:value_test_boolean")
        RedisManager.values.set(booleanKey, false)
        val boolean = RedisManager.values.get<Boolean>(booleanKey)
        assertNotNull(boolean)
        logger.info("$booleanKey=$boolean")

        val incrementKey = RedisKeys.genKey("$keyPrefix:value_test_increment")
        RedisManager.values.increment(incrementKey, 66)
        val increment = RedisManager.values.get<Int>(incrementKey)
        assertNotNull(increment)
        logger.info("$incrementKey=$increment")

        val intKey = RedisKeys.genKey("$keyPrefix:value_test_int")
        RedisManager.values.set(intKey, 1)
        val int = RedisManager.values.get<Int>(intKey)
        assertNotNull(int)
        logger.info("$intKey=$int")

        val longKey = RedisKeys.genKey("$keyPrefix:value_test_long")
        RedisManager.values.set(longKey, Long.MAX_VALUE)
        val long = RedisManager.values.get<Long>(longKey)
        assertNotNull(long)
        logger.info("$longKey=$long")

        val doubleKey = RedisKeys.genKey("$keyPrefix:value_test_double")
        RedisManager.values.set(doubleKey, 1.012)
        val double = RedisManager.values.get<Double>(doubleKey)
        assertNotNull(double)
        logger.info("$doubleKey=$double")

        val stringKey = RedisKeys.genKey("$keyPrefix:value_test_string")
        RedisManager.values.set(stringKey, "test")
        val string = RedisManager.values.get<String>(stringKey)
        assertNotNull(string)
        logger.info("$stringKey=$string")

        val objKey = RedisKeys.genKey("$keyPrefix:value_test_obj")
        RedisManager.values.set(objKey, Person("a", 20))
        val obj = RedisManager.values.get<Person>(objKey)
        assertNotNull(obj)
        logger.info("$objKey=$obj")

        val intEnumKey = RedisKeys.genKey("$keyPrefix:value_test_int_enum")
        RedisManager.values.set(intEnumKey, MyIntEnum.ONE)
        val intEnum = RedisManager.values.get<MyIntEnum>(intEnumKey)
        assertNotNull(intEnum)
        logger.info("$intEnumKey=$intEnum")

        val stringEnumKey = RedisKeys.genKey("$keyPrefix:value_test_string_enum")
        RedisManager.values.set(stringEnumKey, MyStringEnum.YES)
        val stringEnum = RedisManager.values.get<MyStringEnum>(stringEnumKey)
        assertNotNull(stringEnum)
        logger.info("$stringEnumKey=$stringEnum")

        // redis version.
        val redisVersion = RedisManager
            .redisTemplate
            .connectionFactory
            ?.connection
            ?.info("server")
            ?.getProperty("redis_version")
        if (redisVersion?.startsWith("3.2") == true) {
            RedisManager.deleteByKeyPatterns("$keyPrefix:*")
        } else {
            RedisManager.delete(RedisManager.keys("$keyPrefix:*"))
        }
    }

    @Execution(ExecutionMode.CONCURRENT)
    @Test
    fun testRedisMaps(testInfo: TestInfo) {
        val keyPrefix = testInfo.testMethod.get().name
        val testMapKey = RedisKeys.genKey("$keyPrefix:map")

        val booleanKey = RedisKeys.genKey("value_test_boolean")
        RedisManager.maps.put(testMapKey, booleanKey, false)
        val boolean = RedisManager.maps.get<Boolean>(testMapKey, booleanKey)
        logger.info("$testMapKey.$booleanKey=$boolean")

        val intKey = RedisKeys.genKey("value_test_int")
        RedisManager.maps.put(testMapKey, intKey, 1)
        val int = RedisManager.maps.getInt(testMapKey, intKey)
        logger.info("$testMapKey.$intKey=$int")

        val longKey = RedisKeys.genKey("value_test_long")
        RedisManager.maps.put(testMapKey, longKey, Long.MAX_VALUE)
        val long = RedisManager.maps.getLong(testMapKey, longKey)
        logger.info("$testMapKey.$longKey=$long")

        val doubleKey = RedisKeys.genKey("value_test_double")
        RedisManager.maps.put(testMapKey, doubleKey, 1.012)
        val double = RedisManager.maps.getDouble(testMapKey, doubleKey)
        logger.info("$testMapKey.$doubleKey=$double")

        val stringKey = RedisKeys.genKey("value_test_string")
        RedisManager.maps.put(testMapKey, stringKey, "test")
        val string = RedisManager.maps.getString(testMapKey, stringKey)
        logger.info("$testMapKey.$stringKey=$string")

        val objKey = RedisKeys.genKey("value_test_obj")
        RedisManager.maps.putObj(testMapKey, objKey, Person("a", 20))
        val obj = RedisManager.maps.getObj<Person>(testMapKey, objKey)
        logger.info("$testMapKey.$objKey=$obj")

        val intEnumKey = RedisKeys.genKey("value_test_int_enum")
        RedisManager.maps.put(testMapKey, intEnumKey, MyIntEnum.ONE)
        val intEnum = RedisManager.maps.getEnum<MyIntEnum, Int>(testMapKey, intEnumKey)
        logger.info("$testMapKey.$intEnumKey=$intEnum")

        val stringEnumKey = RedisKeys.genKey("value_test_string_enum")
        RedisManager.maps.put(testMapKey, stringEnumKey, MyStringEnum.YES)
        val stringEnum = RedisManager.maps.getEnum<MyStringEnum, String>(testMapKey, stringEnumKey)
        logger.info("$testMapKey.$stringEnumKey=$stringEnum")

        val map = RedisManager.maps.getMap(testMapKey)
        logger.info("$testMapKey=$map")

        val testMapKey2 = RedisKeys.genKey("$keyPrefix:map2")
        RedisManager.maps.putAll(testMapKey2, map)

        val boolean2 = RedisManager.maps.get<Boolean>(testMapKey2, booleanKey)
        logger.info("$testMapKey2.$boolean2=$boolean2")

        val int2 = RedisManager.maps.getInt(testMapKey2, intKey)
        logger.info("$testMapKey2.$int2=$int2")

        val long2 = RedisManager.maps.getLong(testMapKey2, longKey)
        logger.info("$testMapKey2.$long2=$long2")

        val double2 = RedisManager.maps.getDouble(testMapKey2, doubleKey)
        logger.info("$testMapKey2.$double2=$double2")

        val string2 = RedisManager.maps.getString(testMapKey2, stringKey)
        logger.info("$testMapKey2.$string2=$string2")

        val obj2 = RedisManager.maps.getObj<Person>(testMapKey2, objKey)
        logger.info("$testMapKey2.$obj2=$obj2")

        val intEnum2 = RedisManager.maps.getEnum<MyIntEnum, Int>(testMapKey2, intEnumKey)
        logger.info("$testMapKey2.$intEnum2=$intEnum2")

        val stringEnum2 = RedisManager.maps.getEnum<MyStringEnum, String>(testMapKey2, stringEnumKey)
        logger.info("$testMapKey2.$stringEnum2=$stringEnum2")

        val map2 = RedisManager.maps.getMap(testMapKey2)
        logger.info("$testMapKey2.$map2=$map2")

        RedisManager.deleteByKeyPatterns("$keyPrefix:*")
    }

    @Execution(ExecutionMode.CONCURRENT)
    @Test
    fun testList(testInfo: TestInfo) {
        val keyPrefix = testInfo.testMethod.get().name

        RedisManager.doInTransaction {
            RedisManager.lists.rightPushString(keyPrefix, "1")
            RedisManager.lists.rightPushString(keyPrefix, "1")
            RedisManager.lists.rightPushString(keyPrefix, "1")
            RedisManager.lists.rightPushString(keyPrefix, "1")
            RedisManager.lists.rightPushString(keyPrefix, "1")
            RedisManager.lists.rightPushString(keyPrefix, "1")
        }
        RedisManager.deleteByKeyPatterns("$keyPrefix:*")
    }

    @Execution(ExecutionMode.CONCURRENT)
    @Test
    fun testListener(testInfo: TestInfo) {
        val keyPrefix = testInfo.testMethod.get().name

        RedisManager.values.set("$keyPrefix:testExpire0", "year")
        RedisManager.values.set("$keyPrefix:testExpire1", "year", 1)
        RedisManager.values.set("$keyPrefix:testExpire2", "year", 2)
        RedisManager.values.set("$keyPrefix:testExpire3", "year", 3)
        RedisManager.values.set("$keyPrefix:testExpire4", "year", 4)

        Thread.sleep(10 * 1000)

        RedisManager.deleteByKeyPatterns("$keyPrefix:*")
    }

    @Execution(ExecutionMode.CONCURRENT)
    @RepeatedTest(100)
    fun testMulti(testInfo: TestInfo, repetitionInfo: RepetitionInfo) {
        val keyPrefix = testInfo.testMethod.get().name + repetitionInfo.currentRepetition
        Assertions.assertThrows(BizException::class.java) {
            val result = RedisManager.doInTransaction {
                RedisManager.values.set("$keyPrefix:Multi a", "a")
                RedisManager.values.set("$keyPrefix:Multi b", "b")
                RedisManager.values.set("$keyPrefix:Multi c", "c")
                RedisManager.deleteByKeyPatterns("$keyPrefix:*")
            }
            logger.info(result.toString())

            RedisManager.doInTransaction {
                RedisManager.values.set("$keyPrefix:2 Multi a", "a")
                RedisManager.values.set("$keyPrefix:2 Multi b", "b")
                throw BizException("")
            }
        }
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

    companion object : IntEnumCreator(MyIntEnum::class.java) {
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

    companion object : StringEnumCreator(MyStringEnum::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: String) =
            super.create(value)
    }
}
