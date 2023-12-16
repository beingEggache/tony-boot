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

package com.tony.test.redis.protostuff

import com.tony.redis.RedisKeys
import com.tony.redis.RedisManager
import com.tony.test.redis.RedisTestIntEnum
import com.tony.test.redis.RedisTestStringEnum
import com.tony.test.redis.SimpleObj
import com.tony.test.redis.TestRedisApp
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
@Suppress("SpringBootApplicationProperties")
@SpringBootTest(
    properties = [
        "redis.serializerMode=PROTOSTUFF",
    ],
    classes = [TestRedisApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
class ProtostuffRedisMapTests {

    private val logger = LoggerFactory.getLogger(ProtostuffRedisMapTests::class.java)


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
        val int = RedisManager.maps.get<Int>(testMapKey, intKey)
        logger.info("$testMapKey.$intKey=$int")

        val longKey = RedisKeys.genKey("value_test_long")
        RedisManager.maps.put(testMapKey, longKey, Long.MAX_VALUE)
        val long = RedisManager.maps.get<Long>(testMapKey, longKey)
        logger.info("$testMapKey.$longKey=$long")

        val doubleKey = RedisKeys.genKey("value_test_double")
        RedisManager.maps.put(testMapKey, doubleKey, 1.012)
        val double = RedisManager.maps.get<Double>(testMapKey, doubleKey)
        logger.info("$testMapKey.$doubleKey=$double")

        val stringKey = RedisKeys.genKey("value_test_string")
        RedisManager.maps.put(testMapKey, stringKey, "test")
        val string = RedisManager.maps.get<String>(testMapKey, stringKey)
        logger.info("$testMapKey.$stringKey=$string")

        val objKey = RedisKeys.genKey("value_test_obj")
        RedisManager.maps.put(testMapKey, objKey, SimpleObj("a", 20))
        val obj = RedisManager.maps.get<SimpleObj>(testMapKey, objKey)
        logger.info("$testMapKey.$objKey=$obj")

        val intEnumKey = RedisKeys.genKey("value_test_int_enum")
        RedisManager.maps.put(testMapKey, intEnumKey, RedisTestIntEnum.ONE)
        val intEnum = RedisManager.maps.get<RedisTestIntEnum>(testMapKey, intEnumKey)
        logger.info("$testMapKey.$intEnumKey=$intEnum")

        val stringEnumKey = RedisKeys.genKey("value_test_string_enum")
        RedisManager.maps.put(testMapKey, stringEnumKey, RedisTestStringEnum.YES)
        val stringEnum = RedisManager.maps.get<RedisTestStringEnum>(testMapKey, stringEnumKey)
        logger.info("$testMapKey.$stringEnumKey=$stringEnum")

        val map = RedisManager.maps.entries(testMapKey)
        logger.info("$testMapKey=$map")

        val testMapKey2 = RedisKeys.genKey("$keyPrefix:map2")
        RedisManager.maps.putAll(testMapKey2, map)

        val boolean2 = RedisManager.maps.get<Boolean>(testMapKey2, booleanKey)
        logger.info("$testMapKey2.$boolean2=$boolean2")

        val int2 = RedisManager.maps.get<Int>(testMapKey2, intKey)
        logger.info("$testMapKey2.$int2=$int2")

        val long2 = RedisManager.maps.get<Long>(testMapKey2, longKey)
        logger.info("$testMapKey2.$long2=$long2")

        val double2 = RedisManager.maps.get<Double>(testMapKey2, doubleKey)
        logger.info("$testMapKey2.$double2=$double2")

        val string2 = RedisManager.maps.get<String>(testMapKey2, stringKey)
        logger.info("$testMapKey2.$string2=$string2")

        val obj2 = RedisManager.maps.get<SimpleObj>(testMapKey2, objKey)
        logger.info("$testMapKey2.$obj2=$obj2")

        val intEnum2 = RedisManager.maps.get<RedisTestIntEnum>(testMapKey2, intEnumKey)
        logger.info("$testMapKey2.$intEnum2=$intEnum2")

        val stringEnum2 = RedisManager.maps.get<RedisTestStringEnum>(testMapKey2, stringEnumKey)
        logger.info("$testMapKey2.$stringEnum2=$stringEnum2")

        val map2 = RedisManager.maps.entries(testMapKey2)
        logger.info("$testMapKey2.$map2=$map2")

        RedisManager.deleteByKeyPatterns("$keyPrefix:*")
    }
}

