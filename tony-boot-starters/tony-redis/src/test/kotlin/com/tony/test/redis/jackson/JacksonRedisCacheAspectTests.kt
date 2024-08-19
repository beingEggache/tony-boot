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

package com.tony.test.redis.jackson

import com.tony.redis.RedisKeys
import com.tony.redis.RedisManager
import com.tony.test.redis.ObjWithList
import com.tony.test.redis.ObjWithMap
import com.tony.test.redis.ObjWithObjList
import com.tony.test.redis.ObjWithObjMap
import com.tony.test.redis.RedisTestIntEnum
import com.tony.test.redis.RedisTestStringEnum
import com.tony.test.redis.SimpleObj
import com.tony.test.redis.TestRedisApp
import com.tony.test.redis.jackson.service.JacksonRedisCacheAspectService
import com.tony.test.redis.jackson.service.JacksonRedisCacheAspectService.Companion.cacheKeyTemplate
import com.tony.utils.getLogger
import com.tony.utils.toJsonString
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.math.BigInteger
import java.util.Locale


@SpringBootTest(
    properties = ["redis.serializerMode=JACKSON"],
    classes = [TestRedisApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
class JacksonRedisCacheAspectTests {

    private val logger = getLogger()

    @Resource
    private lateinit var jacksonRedisCacheAspectService: JacksonRedisCacheAspectService

    @Test
    fun testBoolean() {
        testInternal<Boolean> { key ->
            jacksonRedisCacheAspectService.testBoolean(key, null as Boolean?)
        }
        testInternal<Boolean> { key ->
            jacksonRedisCacheAspectService.testBoolean(key, true)
        }
        testInternal<Boolean> { key ->
            jacksonRedisCacheAspectService.testBoolean(key, false)
        }
    }

    @Test
    fun testNumber() {
        testInternal<Int> { key ->
            jacksonRedisCacheAspectService.testNumber(key, null as Int?)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testNumber(key, Byte.MAX_VALUE)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testNumber(key, Short.MAX_VALUE)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testNumber(key, Int.MAX_VALUE)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testNumber(key, Long.MAX_VALUE)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testNumber(key, Float.MAX_VALUE)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testNumber(key, Double.MAX_VALUE)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testNumber(key, BigDecimal.ONE)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testNumber(key, BigInteger.ONE)
        }

    }

    @Test
    fun testString() {
        testInternal { key ->
            jacksonRedisCacheAspectService.testString(key, null)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testString(key, "")
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testString(key, "hello")
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testString(key, "aloha")
        }
    }

    @Test
    fun testEnum() {
        testInternal<RedisTestIntEnum> { key ->
            jacksonRedisCacheAspectService.testIntEnum(key, null)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testIntEnum(key, RedisTestIntEnum.ZERO)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testIntEnum(key, RedisTestIntEnum.ONE)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testStringEnum(key, RedisTestStringEnum.YES)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testStringEnum(key, RedisTestStringEnum.NO)
        }
    }

    @Test
    fun testArray() {
        testInternal("array:null") { key ->
            jacksonRedisCacheAspectService.testArray(key, null)
        }
        testInternal("array:byte") { key ->
            jacksonRedisCacheAspectService.testArray(key, arrayOf(Byte.MAX_VALUE))
        }
        testInternal("array:short") { key ->
            jacksonRedisCacheAspectService.testArray(key, arrayOf(Short.MAX_VALUE))
        }
        testInternal("array:int") { key ->
            jacksonRedisCacheAspectService.testArray(key, arrayOf(Int.MAX_VALUE))
        }
        testInternal("array:long") { key ->
            jacksonRedisCacheAspectService.testArray(key, arrayOf(Long.MAX_VALUE))
        }
        testInternal("array:float") { key ->
            jacksonRedisCacheAspectService.testArray(key, arrayOf(Float.MAX_VALUE))
        }
        testInternal("array:double") { key ->
            jacksonRedisCacheAspectService.testArray(key, arrayOf(Double.MAX_VALUE))
        }
        testInternal("array:bigDecimal") { key ->
            jacksonRedisCacheAspectService.testArray(key, arrayOf(BigDecimal.ONE))
        }
        testInternal("array:bigInteger") { key ->
            jacksonRedisCacheAspectService.testArray(key, arrayOf(BigInteger.ONE))
        }
        testInternal("array:obj") { key ->
            jacksonRedisCacheAspectService.testArray(key, arrayOf(SimpleObj("tony", 18)))
        }
        testInternal("array:list") { key ->
            jacksonRedisCacheAspectService.testArray(key, arrayOf(listOf(SimpleObj("tony", 18))))
        }
        testInternal("array:map") { key ->
            jacksonRedisCacheAspectService.testArray(key, arrayOf(mapOf("a" to SimpleObj("tony", 18))))
        }
    }

    @Test
    fun testList() {
        testInternal("list:null") { key ->
            jacksonRedisCacheAspectService.testList(key, null)
        }
        testInternal("list:byte") { key ->
            jacksonRedisCacheAspectService.testList(key, listOf(Byte.MAX_VALUE))
        }
        testInternal("list:short") { key ->
            jacksonRedisCacheAspectService.testList(key, listOf(Short.MAX_VALUE))
        }
        testInternal("list:int") { key ->
            jacksonRedisCacheAspectService.testList(key, listOf(Int.MAX_VALUE))
        }
        testInternal("list:long") { key ->
            jacksonRedisCacheAspectService.testList(key, listOf(Long.MAX_VALUE))
        }
        testInternal("list:float") { key ->
            jacksonRedisCacheAspectService.testList(key, listOf(Float.MAX_VALUE))
        }
        testInternal("list:double") { key ->
            jacksonRedisCacheAspectService.testList(key, listOf(Double.MAX_VALUE))
        }
        testInternal("list:bigDecimal") { key ->
            jacksonRedisCacheAspectService.testList(key, listOf(BigDecimal.ONE))
        }
        testInternal("list:bigInteger") { key ->
            jacksonRedisCacheAspectService.testList(key, listOf(BigInteger.ONE))
        }
        testInternal("list:obj") { key ->
            jacksonRedisCacheAspectService.testList(key, listOf(SimpleObj("tony", 18)))
        }
        testInternal("list:array") { key ->
            jacksonRedisCacheAspectService.testList(key, listOf(arrayOf(SimpleObj("tony", 18))))
        }
        testInternal("list:map") { key ->
            jacksonRedisCacheAspectService.testList(key, listOf(mapOf("a" to SimpleObj("tony", 18))))
        }
    }

    @Test
    fun testMap() {
        testInternal("map:null") { key ->
            jacksonRedisCacheAspectService.testMap(key, null)
        }
        testInternal("map:byte") { key ->
            jacksonRedisCacheAspectService.testMap(key, mapOf("key" to Byte.MAX_VALUE))
        }
        testInternal("map:short") { key ->
            jacksonRedisCacheAspectService.testMap(key, mapOf("key" to (Short.MAX_VALUE)))
        }
        testInternal("map:int") { key ->
            jacksonRedisCacheAspectService.testMap(key, mapOf("key" to (Int.MAX_VALUE)))
        }
        testInternal("map:long") { key ->
            jacksonRedisCacheAspectService.testMap(key, mapOf("key" to (Long.MAX_VALUE)))
        }
        testInternal("map:float") { key ->
            jacksonRedisCacheAspectService.testMap(key, mapOf("key" to (Float.MAX_VALUE)))
        }
        testInternal("map:double") { key ->
            jacksonRedisCacheAspectService.testMap(key, mapOf("key" to (Double.MAX_VALUE)))
        }
        testInternal("map:bigDecimal") { key ->
            jacksonRedisCacheAspectService.testMap(key, mapOf("key" to (BigDecimal.ONE)))
        }
        testInternal("map:bigInteger") { key ->
            jacksonRedisCacheAspectService.testMap(key, mapOf("key" to (BigInteger.ONE)))
        }
        testInternal("map:obj") { key ->
            jacksonRedisCacheAspectService.testMap(key, mapOf("key" to (SimpleObj("tony", 18))))
        }
        testInternal("map:array") { key ->
            jacksonRedisCacheAspectService.testMap(key, mapOf("key" to (arrayOf(SimpleObj("tony", 18)))))
        }
        testInternal("map:map") { key ->
            jacksonRedisCacheAspectService.testMap(key, mapOf("key" to (mapOf("a" to SimpleObj("tony", 18)))))
        }
    }

    @Test
    fun testObj() {
        testInternal("obj:null") { key ->
            jacksonRedisCacheAspectService.testObj(key, null)
        }
        testInternal("obj:SimpleObj") { key ->
            jacksonRedisCacheAspectService.testObj(key, SimpleObj("tony", 18))
        }
        testInternal("obj:ObjWithList") { key ->
            jacksonRedisCacheAspectService.testObj(key, ObjWithList("tony", listOf("string")))
        }
        testInternal("obj:ObjWithMap") { key ->
            jacksonRedisCacheAspectService.testObj(key, ObjWithMap("tony", mapOf("key" to SimpleObj("tony", 18))))
        }
        testInternal("obj:ObjWithObjList") { key ->
            jacksonRedisCacheAspectService.testObj(key, ObjWithObjList("tony", listOf(SimpleObj("tony", 18))))
        }
        testInternal("obj:ObjWithObjMap") { key ->
            jacksonRedisCacheAspectService.testObj(key, ObjWithObjMap("tony", mapOf("key" to ObjWithObjList("tony", listOf(SimpleObj("tony", 18))))))
        }
    }

    private inline fun <reified T : Any> testInternal(crossinline func: (String) -> T?) {
        val simpleName = T::class.java.simpleName
        val key = simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) }
        val methodValue = func(key)
        val cacheKey = RedisKeys.genKey(cacheKeyTemplate, key, methodValue)
        logger.info("$key key: $cacheKey")
        func(key)
        logger.info("$key value:${methodValue.toJsonString()}")
        val cacheValue = RedisManager.values.get<T>(cacheKey)
        check(methodValue == cacheValue) {
            "type:${simpleName}, methodValue: $methodValue, cacheValue: $cacheValue"
        }
        RedisManager.delete(cacheKey)
    }

    private inline fun <reified T : Any> testInternal(key: String, crossinline func: (String) -> T?) {
        val methodValue = func(key)
        val cacheKey = RedisKeys.genKey("${JacksonRedisCacheAspectService.cacheKeyPrefix}:%s", key)
        logger.info("$key key: $cacheKey")
        func(key)
        logger.info("$key value:${methodValue.toJsonString()}")
        val cacheValue = RedisManager.values.get<T>(cacheKey)
        logger.info("type:${T::class.java.simpleName}, methodValue: ${methodValue.toJsonString()},cacheValue: ${cacheValue.toJsonString()}")
        RedisManager.delete(cacheKey)
    }
}
