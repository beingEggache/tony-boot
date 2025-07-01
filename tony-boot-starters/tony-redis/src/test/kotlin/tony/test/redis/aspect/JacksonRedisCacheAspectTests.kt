package tony.test.redis.aspect

import jakarta.annotation.Resource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.boot.test.context.SpringBootTest
import tony.test.redis.util.SimpleObj
import tony.test.redis.util.TestIntEnum
import tony.test.redis.util.TestStringEnum
import java.math.BigDecimal
import tony.redis.RedisManager
import tony.test.redis.TestRedisApplication
import tony.utils.asTo

/**
 * JacksonRedisCacheAspect 切面功能测试
 *
 * 覆盖基础类型、对象、枚举、List、Map、缓存命中、失效、key 生成、类型转换、过期、复杂表达式等场景
 *
 * @author tony
 * @date 2025/07/01 17:00
 */
@SpringBootTest(classes = [TestRedisApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@DisplayName("JacksonRedisCacheAspect 切面功能测试")
class JacksonRedisCacheAspectTests {

    @Resource
    private lateinit var service: JacksonRedisCacheAspectService

    companion object {
        @JvmStatic
        fun cacheCases() = listOf(
            arrayOf("bool", true, { a: Any?, b: Any? -> assertEquals(a, b) }),
            arrayOf("int", 123, { a: Any?, b: Any? -> assertEquals(a, b) }),
            arrayOf("long", 123456789L, { a: Any?, b: Any? -> assertEquals(a, b) }),
            arrayOf("bigdecimal", BigDecimal("123.45"), { a: Any?, b: Any? -> assertEquals(a, b) }),
            arrayOf("string", "hello", { a: Any?, b: Any? -> assertEquals(a, b) }),
            arrayOf("obj", SimpleObj("tony", 18), { a: Any?, b: Any? -> assertEquals(a, b) }),
            arrayOf("intEnum", TestIntEnum.ACTIVE, { a: Any?, b: Any? -> assertEquals(a, b) }),
            arrayOf("stringEnum", TestStringEnum.YES, { a: Any?, b: Any? -> assertEquals(a, b) }),
            arrayOf("list", listOf(1, 2, 3), { a: Any?, b: Any? -> assertEquals(a, b) }),
            arrayOf("map", mapOf("a" to 1, "b" to 2), { a: Any?, b: Any? -> assertEquals(a, b) })
        )
    }

    @Order(1)
    @ParameterizedTest
    @MethodSource("cacheCases")
    @DisplayName("缓存命中与类型一致性")
    fun testCacheable(key: String, value: Any?, assertBlock: (Any?, Any?) -> Unit) {
        // set: key, value
        val result = when (value) {
            is Boolean? -> service.cacheBoolean(key, value)
            is Int? -> service.cacheInt(key, value)
            is Long? -> service.cacheLong(key, value)
            is BigDecimal? -> service.cacheBigDecimal(key, value)
            is String? -> service.cacheString(key, value)
            is SimpleObj? -> service.cacheObj(key, value)
            is TestIntEnum? -> service.cacheIntEnum(key, value)
            is TestStringEnum? -> service.cacheStringEnum(key, value)
            is List<*>? -> service.cacheList(key, value.asTo())
            is Map<*, *>? -> service.cacheMap(key, value.asTo())
            else -> throw IllegalArgumentException("不支持的类型: ${value.javaClass}")
        }
        assertBlock(value, result)
        // get: key, value（必须与 set 完全一致，才能命中缓存）
        val cached = when (value) {
            is Boolean? -> service.cacheBoolean(key, value)
            is Int? -> service.cacheInt(key, value)
            is Long? -> service.cacheLong(key, value)
            is BigDecimal? -> service.cacheBigDecimal(key, value)
            is String? -> service.cacheString(key, value)
            is SimpleObj? -> service.cacheObj(key, value)
            is TestIntEnum? -> service.cacheIntEnum(key, value)
            is TestStringEnum? -> service.cacheStringEnum(key, value)
            is List<*>? -> service.cacheList(key, value.asTo())
            is Map<*, *>? -> service.cacheMap(key, value.asTo())
            else -> throw IllegalArgumentException("不支持的类型: ${value.javaClass}")
        }
        assertBlock(value, cached)
    }

    @Order(2)
    @Test
    @DisplayName("缓存失效（Evict）")
    fun testCacheEvict() {
        val key = "evictKey"
        val value = "toBeEvicted"
        service.cacheString(key, value)
        val redisKey = "cacheKeyPrefix:$key:$value"
        assertEquals(value, RedisManager.values.get<String>(redisKey))
        service.evictCache(key, value)
        assertNull(RedisManager.values.get<String>(redisKey))
    }

    @Order(3)
    @Test
    @DisplayName("缓存过期（expire）功能")
    fun testCacheExpire() {
        val key = "expireKey"
        val value = "expireValue"
        service.cacheWithExpire(key, value)
        val redisKey = "cacheKeyPrefix:$key:$value"
        assertEquals(value, RedisManager.values.get<String>(redisKey))
        Thread.sleep(1100)
        assertNull(RedisManager.values.get<String>(redisKey))
    }

    @Order(4)
    @Test
    @DisplayName("复杂表达式 key 生成与命中")
    fun testComplexExpression() {
        val order = JacksonRedisCacheAspectService.Order(
            id = 1001L,
            user = JacksonRedisCacheAspectService.User(99L, "张三"),
            items = listOf("apple", "banana")
        )
        val expected = "order:1001:99:apple"
        val result = service.cacheOrder(order)
        assertEquals(expected, result)
        // 再次调用，命中缓存
        val cached = service.cacheOrder(order)
        assertEquals(expected, cached)
    }

    @Order(5)
    @Test
    @DisplayName("可重复Evict及复杂表达式失效")
    fun testMultiEvict() {
        val key = "multiEvictKey"
        val v1 = "v1"
        val v2 = "v2"
        service.cacheString(key, v1)
        service.cacheString(key, v2)
        val redisKey1 = "cacheKeyPrefix:$key:$v1"
        val redisKey2 = "cacheKeyPrefix:$key:$v2"
        assertEquals(v1, RedisManager.values.get<String>(redisKey1))
        assertEquals(v2, RedisManager.values.get<String>(redisKey2))
        service.evictMulti(key, v1, v2)
        assertNull(RedisManager.values.get<String>(redisKey1))
        assertNull(RedisManager.values.get<String>(redisKey2))
    }
}
