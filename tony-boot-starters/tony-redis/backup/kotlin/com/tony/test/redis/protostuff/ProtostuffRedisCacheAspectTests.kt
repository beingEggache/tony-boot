package tony.test.redis.protostuff

import tony.redis.RedisKeys
import tony.redis.RedisManager
import tony.test.redis.ObjWithList
import tony.test.redis.ObjWithMap
import tony.test.redis.ObjWithObjList
import tony.test.redis.ObjWithObjMap
import tony.test.redis.RedisTestIntEnum
import tony.test.redis.RedisTestStringEnum
import tony.test.redis.SimpleObj
import tony.test.redis.TestRedisApp
import tony.test.redis.protostuff.service.ProtostuffRedisCacheAspectService
import tony.test.redis.protostuff.service.ProtostuffRedisCacheAspectService.Companion.cacheKeyTemplate
import tony.core.utils.getLogger
import tony.core.utils.toJsonString
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.math.BigInteger
import java.util.Locale


@SpringBootTest(
    properties = ["redis.serializerMode=PROTOSTUFF"],
    classes = [TestRedisApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
class ProtostuffRedisCacheAspectTests {

    private val logger = getLogger()

    @Resource
    private lateinit var protostuffRedisCacheAspectService: ProtostuffRedisCacheAspectService

    @Test
    fun testBoolean() {
        testInternal<Boolean> { key ->
            protostuffRedisCacheAspectService.testBoolean(key, null as Boolean?)
        }
        testInternal<Boolean> { key ->
            protostuffRedisCacheAspectService.testBoolean(key, true)
        }
        testInternal<Boolean> { key ->
            protostuffRedisCacheAspectService.testBoolean(key, false)
        }
    }

    @Test
    fun testNumber() {
        testInternal<Int> { key ->
            protostuffRedisCacheAspectService.testNumber(key, null as Int?)
        }
        testInternal { key ->
            protostuffRedisCacheAspectService.testNumber(key, Byte.MAX_VALUE)
        }
        testInternal { key ->
            protostuffRedisCacheAspectService.testNumber(key, Short.MAX_VALUE)
        }
        testInternal { key ->
            protostuffRedisCacheAspectService.testNumber(key, Int.MAX_VALUE)
        }
        testInternal { key ->
            protostuffRedisCacheAspectService.testNumber(key, Long.MAX_VALUE)
        }
        testInternal { key ->
            protostuffRedisCacheAspectService.testNumber(key, Float.MAX_VALUE)
        }
        testInternal { key ->
            protostuffRedisCacheAspectService.testNumber(key, Double.MAX_VALUE)
        }
        testInternal { key ->
            protostuffRedisCacheAspectService.testNumber(key, BigDecimal.ONE)
        }
        testInternal { key ->
            protostuffRedisCacheAspectService.testNumber(key, BigInteger.ONE)
        }

    }

    @Test
    fun testString() {
        testInternal { key ->
            protostuffRedisCacheAspectService.testString(key, null)
        }
        testInternal { key ->
            protostuffRedisCacheAspectService.testString(key, "")
        }
        testInternal { key ->
            protostuffRedisCacheAspectService.testString(key, "hello")
        }
        testInternal { key ->
            protostuffRedisCacheAspectService.testString(key, "aloha")
        }
    }

    @Test
    fun testEnum() {
        testInternal<RedisTestIntEnum> { key ->
            protostuffRedisCacheAspectService.testIntEnum(key, null)
        }
        testInternal { key ->
            protostuffRedisCacheAspectService.testIntEnum(key, RedisTestIntEnum.ZERO)
        }
        testInternal { key ->
            protostuffRedisCacheAspectService.testIntEnum(key, RedisTestIntEnum.ONE)
        }
        testInternal { key ->
            protostuffRedisCacheAspectService.testStringEnum(key, RedisTestStringEnum.YES)
        }
        testInternal { key ->
            protostuffRedisCacheAspectService.testStringEnum(key, RedisTestStringEnum.NO)
        }
    }

    @Test
    fun testArray() {
        testInternal("array:null") { key ->
            protostuffRedisCacheAspectService.testArray(key, null)
        }
        // TODO io.protostuff.runtime.ArraySchemas.getSchema(ArraySchemas.java:149) Should not happen.
        testInternal("array:byte") { key ->
            protostuffRedisCacheAspectService.testArray(key, arrayOf(Byte.MAX_VALUE))
        }
        testInternal("array:short") { key ->
            protostuffRedisCacheAspectService.testArray(key, arrayOf(Short.MAX_VALUE))
        }
        testInternal("array:int") { key ->
            protostuffRedisCacheAspectService.testArray(key, arrayOf(Int.MAX_VALUE))
        }
        testInternal("array:long") { key ->
            protostuffRedisCacheAspectService.testArray(key, arrayOf(Long.MAX_VALUE))
        }
        testInternal("array:float") { key ->
            protostuffRedisCacheAspectService.testArray(key, arrayOf(Float.MAX_VALUE))
        }
        testInternal("array:double") { key ->
            protostuffRedisCacheAspectService.testArray(key, arrayOf(Double.MAX_VALUE))
        }
        testInternal("array:bigDecimal") { key ->
            protostuffRedisCacheAspectService.testArray(key, arrayOf(BigDecimal.ONE))
        }
        testInternal("array:bigInteger") { key ->
            protostuffRedisCacheAspectService.testArray(key, arrayOf(BigInteger.ONE))
        }
        testInternal("array:obj") { key ->
            protostuffRedisCacheAspectService.testArray(key, arrayOf(SimpleObj("tony", 18)))
        }
        testInternal("array:list") { key ->
            protostuffRedisCacheAspectService.testArray(key, arrayOf(listOf(SimpleObj("tony", 18))))
        }
        testInternal("array:map") { key ->
            protostuffRedisCacheAspectService.testArray(key, arrayOf(mapOf("a" to SimpleObj("tony", 18))))
        }
    }

    @Test
    fun testList() {
        testInternal("list:null") { key ->
            protostuffRedisCacheAspectService.testList(key, null)
        }
        testInternal("list:byte") { key ->
            protostuffRedisCacheAspectService.testList(key, listOf(Byte.MAX_VALUE))
        }
        testInternal("list:short") { key ->
            protostuffRedisCacheAspectService.testList(key, listOf(Short.MAX_VALUE))
        }
        testInternal("list:int") { key ->
            protostuffRedisCacheAspectService.testList(key, listOf(Int.MAX_VALUE))
        }
        testInternal("list:long") { key ->
            protostuffRedisCacheAspectService.testList(key, listOf(Long.MAX_VALUE))
        }
        testInternal("list:float") { key ->
            protostuffRedisCacheAspectService.testList(key, listOf(Float.MAX_VALUE))
        }
        testInternal("list:double") { key ->
            protostuffRedisCacheAspectService.testList(key, listOf(Double.MAX_VALUE))
        }
        testInternal("list:bigDecimal") { key ->
            protostuffRedisCacheAspectService.testList(key, listOf(BigDecimal.ONE))
        }
        testInternal("list:bigInteger") { key ->
            protostuffRedisCacheAspectService.testList(key, listOf(BigInteger.ONE))
        }
        testInternal("list:obj") { key ->
            protostuffRedisCacheAspectService.testList(key, listOf(SimpleObj("tony", 18)))
        }
        testInternal("list:array") { key ->
            protostuffRedisCacheAspectService.testList(key, listOf(arrayOf(SimpleObj("tony", 18))))
        }
        testInternal("list:map") { key ->
            protostuffRedisCacheAspectService.testList(key, listOf(mapOf("a" to SimpleObj("tony", 18))))
        }
    }

    @Test
    fun testMap() {
        testInternal("map:null") { key ->
            protostuffRedisCacheAspectService.testMap(key, null)
        }
        testInternal("map:byte") { key ->
            protostuffRedisCacheAspectService.testMap(key, mapOf("key" to Byte.MAX_VALUE))
        }
        testInternal("map:short") { key ->
            protostuffRedisCacheAspectService.testMap(key, mapOf("key" to (Short.MAX_VALUE)))
        }
        testInternal("map:int") { key ->
            protostuffRedisCacheAspectService.testMap(key, mapOf("key" to (Int.MAX_VALUE)))
        }
        testInternal("map:long") { key ->
            protostuffRedisCacheAspectService.testMap(key, mapOf("key" to (Long.MAX_VALUE)))
        }
        testInternal("map:float") { key ->
            protostuffRedisCacheAspectService.testMap(key, mapOf("key" to (Float.MAX_VALUE)))
        }
        testInternal("map:double") { key ->
            protostuffRedisCacheAspectService.testMap(key, mapOf("key" to (Double.MAX_VALUE)))
        }
        testInternal("map:bigDecimal") { key ->
            protostuffRedisCacheAspectService.testMap(key, mapOf("key" to (BigDecimal.ONE)))
        }
        testInternal("map:bigInteger") { key ->
            protostuffRedisCacheAspectService.testMap(key, mapOf("key" to (BigInteger.ONE)))
        }
        testInternal("map:obj") { key ->
            protostuffRedisCacheAspectService.testMap(key, mapOf("key" to (SimpleObj("tony", 18))))
        }
        testInternal("map:array") { key ->
            protostuffRedisCacheAspectService.testMap(key, mapOf("key" to (arrayOf(SimpleObj("tony", 18)))))
        }
        testInternal("map:map") { key ->
            protostuffRedisCacheAspectService.testMap(key, mapOf("key" to (mapOf("a" to SimpleObj("tony", 18)))))
        }
    }

    @Test
    fun testObj() {
        testInternal("obj:null") { key ->
            protostuffRedisCacheAspectService.testObj(key, null)
        }
        testInternal("obj:SimpleObj") { key ->
            protostuffRedisCacheAspectService.testObj(key, SimpleObj("tony", 18))
        }
        testInternal("obj:ObjWithList") { key ->
            protostuffRedisCacheAspectService.testObj(key, ObjWithList("tony", listOf("string")))
        }
        testInternal("obj:ObjWithMap") { key ->
            protostuffRedisCacheAspectService.testObj(key, ObjWithMap("tony", mapOf("key" to SimpleObj("tony", 18))))
        }
        testInternal("obj:ObjWithObjList") { key ->
            protostuffRedisCacheAspectService.testObj(key, ObjWithObjList("tony", listOf(SimpleObj("tony", 18))))
        }
        testInternal("obj:ObjWithObjMap") { key ->
            protostuffRedisCacheAspectService.testObj(key, ObjWithObjMap("tony", mapOf("key" to ObjWithObjList("tony", listOf(SimpleObj("tony", 18))))))
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
        val cacheValue = func(key)
        check(methodValue == cacheValue) {
            "type:${simpleName}, methodValue: $methodValue, cacheValue: $cacheValue"
        }
        RedisManager.delete(cacheKey)
    }

    private inline fun <reified T : Any> testInternal(key: String, crossinline func: (String) -> T?) {
        val methodValue = func(key)
        val cacheKey = RedisKeys.genKey("${ProtostuffRedisCacheAspectService.cacheKeyPrefix}:%s", key)
        logger.info("$key key: $cacheKey")
        func(key)
        logger.info("$key value:${methodValue.toJsonString()}")
        val cacheValue = func(key)
        logger.info("type:${T::class.java.simpleName}, methodValue: ${methodValue.toJsonString()},cacheValue: ${cacheValue.toJsonString()}")
        RedisManager.delete(cacheKey)
    }
}
