package tony.test.redis.fury

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
import tony.test.redis.fury.service.FuryRedisCacheAspectService
import tony.test.redis.protostuff.service.ProtostuffRedisCacheAspectService
import tony.test.redis.protostuff.service.ProtostuffRedisCacheAspectService.Companion.cacheKeyTemplate
import tony.utils.getLogger
import tony.utils.toJsonString
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.math.BigInteger
import java.util.Locale


@SpringBootTest(
    properties = ["redis.serializerMode=FURY"],
    classes = [TestRedisApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
class FuryRedisCacheAspectTests {

    private val logger = getLogger()

    @Resource
    private lateinit var furyRedisCacheAspectService: FuryRedisCacheAspectService

    @Test
    fun testBoolean() {
        testInternal<Boolean> { key ->
            furyRedisCacheAspectService.testBoolean(key, null as Boolean?)
        }
        testInternal<Boolean> { key ->
            furyRedisCacheAspectService.testBoolean(key, true)
        }
        testInternal<Boolean> { key ->
            furyRedisCacheAspectService.testBoolean(key, false)
        }
    }

    @Test
    fun testNumber() {
        testInternal<Int> { key ->
            furyRedisCacheAspectService.testNumber(key, null as Int?)
        }
        testInternal { key ->
            furyRedisCacheAspectService.testNumber(key, Byte.MAX_VALUE)
        }
        testInternal { key ->
            furyRedisCacheAspectService.testNumber(key, Short.MAX_VALUE)
        }
        testInternal { key ->
            furyRedisCacheAspectService.testNumber(key, Int.MAX_VALUE)
        }
        testInternal { key ->
            furyRedisCacheAspectService.testNumber(key, Long.MAX_VALUE)
        }
        testInternal { key ->
            furyRedisCacheAspectService.testNumber(key, Float.MAX_VALUE)
        }
        testInternal { key ->
            furyRedisCacheAspectService.testNumber(key, Double.MAX_VALUE)
        }
        testInternal { key ->
            furyRedisCacheAspectService.testNumber(key, BigDecimal.ONE)
        }
        testInternal { key ->
            furyRedisCacheAspectService.testNumber(key, BigInteger.ONE)
        }

    }

    @Test
    fun testString() {
        testInternal { key ->
            furyRedisCacheAspectService.testString(key, null)
        }
        testInternal { key ->
            furyRedisCacheAspectService.testString(key, "")
        }
        testInternal { key ->
            furyRedisCacheAspectService.testString(key, "hello")
        }
        testInternal { key ->
            furyRedisCacheAspectService.testString(key, "aloha")
        }
    }

    @Test
    fun testEnum() {
        testInternal<RedisTestIntEnum> { key ->
            furyRedisCacheAspectService.testIntEnum(key, null)
        }
        testInternal { key ->
            furyRedisCacheAspectService.testIntEnum(key, RedisTestIntEnum.ZERO)
        }
        testInternal { key ->
            furyRedisCacheAspectService.testIntEnum(key, RedisTestIntEnum.ONE)
        }
        testInternal { key ->
            furyRedisCacheAspectService.testStringEnum(key, RedisTestStringEnum.YES)
        }
        testInternal { key ->
            furyRedisCacheAspectService.testStringEnum(key, RedisTestStringEnum.NO)
        }
    }

    @Test
    fun testArray() {
        testInternal("array:null") { key ->
            furyRedisCacheAspectService.testArray(key, null)
        }
        // TODO io.protostuff.runtime.ArraySchemas.getSchema(ArraySchemas.java:149) Should not happen.
        testInternal("array:byte") { key ->
            furyRedisCacheAspectService.testArray(key, arrayOf(Byte.MAX_VALUE))
        }
        testInternal("array:short") { key ->
            furyRedisCacheAspectService.testArray(key, arrayOf(Short.MAX_VALUE))
        }
        testInternal("array:int") { key ->
            furyRedisCacheAspectService.testArray(key, arrayOf(Int.MAX_VALUE))
        }
        testInternal("array:long") { key ->
            furyRedisCacheAspectService.testArray(key, arrayOf(Long.MAX_VALUE))
        }
        testInternal("array:float") { key ->
            furyRedisCacheAspectService.testArray(key, arrayOf(Float.MAX_VALUE))
        }
        testInternal("array:double") { key ->
            furyRedisCacheAspectService.testArray(key, arrayOf(Double.MAX_VALUE))
        }
        testInternal("array:bigDecimal") { key ->
            furyRedisCacheAspectService.testArray(key, arrayOf(BigDecimal.ONE))
        }
        testInternal("array:bigInteger") { key ->
            furyRedisCacheAspectService.testArray(key, arrayOf(BigInteger.ONE))
        }
        testInternal("array:obj") { key ->
            furyRedisCacheAspectService.testArray(key, arrayOf(SimpleObj("tony", 18)))
        }
        testInternal("array:list") { key ->
            furyRedisCacheAspectService.testArray(key, arrayOf(listOf(SimpleObj("tony", 18))))
        }
        testInternal("array:map") { key ->
            furyRedisCacheAspectService.testArray(key, arrayOf(mapOf("a" to SimpleObj("tony", 18))))
        }
    }

    @Test
    fun testList() {
        testInternal("list:null") { key ->
            furyRedisCacheAspectService.testList(key, null)
        }
        testInternal("list:byte") { key ->
            furyRedisCacheAspectService.testList(key, listOf(Byte.MAX_VALUE))
        }
        testInternal("list:short") { key ->
            furyRedisCacheAspectService.testList(key, listOf(Short.MAX_VALUE))
        }
        testInternal("list:int") { key ->
            furyRedisCacheAspectService.testList(key, listOf(Int.MAX_VALUE))
        }
        testInternal("list:long") { key ->
            furyRedisCacheAspectService.testList(key, listOf(Long.MAX_VALUE))
        }
        testInternal("list:float") { key ->
            furyRedisCacheAspectService.testList(key, listOf(Float.MAX_VALUE))
        }
        testInternal("list:double") { key ->
            furyRedisCacheAspectService.testList(key, listOf(Double.MAX_VALUE))
        }
        testInternal("list:bigDecimal") { key ->
            furyRedisCacheAspectService.testList(key, listOf(BigDecimal.ONE))
        }
        testInternal("list:bigInteger") { key ->
            furyRedisCacheAspectService.testList(key, listOf(BigInteger.ONE))
        }
        testInternal("list:obj") { key ->
            furyRedisCacheAspectService.testList(key, listOf(SimpleObj("tony", 18)))
        }
        testInternal("list:array") { key ->
            furyRedisCacheAspectService.testList(key, listOf(arrayOf(SimpleObj("tony", 18))))
        }
        testInternal("list:map") { key ->
            furyRedisCacheAspectService.testList(key, listOf(mapOf("a" to SimpleObj("tony", 18))))
        }
    }

    @Test
    fun testMap() {
        testInternal("map:null") { key ->
            furyRedisCacheAspectService.testMap(key, null)
        }
        testInternal("map:byte") { key ->
            furyRedisCacheAspectService.testMap(key, mapOf("key" to Byte.MAX_VALUE))
        }
        testInternal("map:short") { key ->
            furyRedisCacheAspectService.testMap(key, mapOf("key" to (Short.MAX_VALUE)))
        }
        testInternal("map:int") { key ->
            furyRedisCacheAspectService.testMap(key, mapOf("key" to (Int.MAX_VALUE)))
        }
        testInternal("map:long") { key ->
            furyRedisCacheAspectService.testMap(key, mapOf("key" to (Long.MAX_VALUE)))
        }
        testInternal("map:float") { key ->
            furyRedisCacheAspectService.testMap(key, mapOf("key" to (Float.MAX_VALUE)))
        }
        testInternal("map:double") { key ->
            furyRedisCacheAspectService.testMap(key, mapOf("key" to (Double.MAX_VALUE)))
        }
        testInternal("map:bigDecimal") { key ->
            furyRedisCacheAspectService.testMap(key, mapOf("key" to (BigDecimal.ONE)))
        }
        testInternal("map:bigInteger") { key ->
            furyRedisCacheAspectService.testMap(key, mapOf("key" to (BigInteger.ONE)))
        }
        testInternal("map:obj") { key ->
            furyRedisCacheAspectService.testMap(key, mapOf("key" to (SimpleObj("tony", 18))))
        }
        testInternal("map:array") { key ->
            furyRedisCacheAspectService.testMap(key, mapOf("key" to (arrayOf(SimpleObj("tony", 18)))))
        }
        testInternal("map:map") { key ->
            furyRedisCacheAspectService.testMap(key, mapOf("key" to (mapOf("a" to SimpleObj("tony", 18)))))
        }
    }

    @Test
    fun testObj() {
        testInternal("obj:null") { key ->
            furyRedisCacheAspectService.testObj(key, null)
        }
        testInternal("obj:SimpleObj") { key ->
            furyRedisCacheAspectService.testObj(key, SimpleObj("tony", 18))
        }
        testInternal("obj:ObjWithList") { key ->
            furyRedisCacheAspectService.testObj(key, ObjWithList("tony", listOf("string")))
        }
        testInternal("obj:ObjWithMap") { key ->
            furyRedisCacheAspectService.testObj(key, ObjWithMap("tony", mapOf("key" to SimpleObj("tony", 18))))
        }
        testInternal("obj:ObjWithObjList") { key ->
            furyRedisCacheAspectService.testObj(key, ObjWithObjList("tony", listOf(SimpleObj("tony", 18))))
        }
        testInternal("obj:ObjWithObjMap") { key ->
            furyRedisCacheAspectService.testObj(key, ObjWithObjMap("tony", mapOf("key" to ObjWithObjList("tony", listOf(SimpleObj("tony", 18))))))
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
