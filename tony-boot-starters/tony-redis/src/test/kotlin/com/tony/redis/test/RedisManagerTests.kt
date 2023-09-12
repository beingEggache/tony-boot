package com.tony.redis.test

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.tony.enums.IntEnumValue
import com.tony.enums.StringEnumValue
import com.tony.enums.IntEnumCreator
import com.tony.enums.StringEnumCreator
import com.tony.exception.BizException
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

/**
 *
 * @author Tang Li
 * @date 2021-05-19 15:22
 */

@SpringBootTest(classes = [TestRedisApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RedisManagerTests {

    private val logger = LoggerFactory.getLogger(RedisManagerTests::class.java)

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
) : IntEnumValue {

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
) : StringEnumValue {

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
