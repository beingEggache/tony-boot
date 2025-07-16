package tony.test.redis.aspect

import org.springframework.stereotype.Service
import tony.annotation.redis.RedisCacheEvict
import tony.annotation.redis.RedisCacheable
import tony.test.redis.util.SimpleObj
import tony.test.redis.util.TestIntEnum
import tony.test.redis.util.TestStringEnum
import java.math.BigDecimal
import java.util.Date

/**
 * JacksonRedisCacheAspect 切面测试用 Service
 *
 * 提供多类型方法用于 @RedisCacheable/@RedisCacheEvict 切面测试
 *
 * @author tony
 * @date 2025/07/01 17:00
 */
@Service
class JacksonRedisCacheAspectService {
    companion object {
        const val cacheKeyTemplate = "cacheKeyPrefix:%s:%s"
    }

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun cacheBoolean(key: String, value: Boolean?): Boolean? = value

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun cacheInt(key: String, value: Int?): Int? = value

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun cacheLong(key: String, value: Long?): Long? = value

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun cacheBigDecimal(key: String, value: BigDecimal?): BigDecimal? = value

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun cacheString(key: String, value: String?): String? = value

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun cacheDate(key: String, value: Date?): Date? = value

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun cacheObj(key: String, value: SimpleObj?): SimpleObj? = value

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun cacheIntEnum(key: String, value: TestIntEnum?): TestIntEnum? = value

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun cacheStringEnum(key: String, value: TestStringEnum?): TestStringEnum? = value

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun cacheList(key: String, value: List<Any?>?): List<Any?>? = value

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun cacheMap(key: String, value: Map<String, Any?>?): Map<String, Any?>? = value

    @RedisCacheEvict(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun evictCache(key: String, value: String?) {}

    // 支持过期时间的缓存
    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"], expire = 1)
    fun cacheWithExpire(key: String, value: String?): String? = value

    // 支持复杂表达式（对象属性、嵌套、拼接等）
    data class User(val id: Long, val name: String)
    data class Order(val id: Long, val user: User, val items: List<String>)

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["order.user.id", "order.items[0]"])
    fun cacheOrder(order: Order): String = "order:${order.id}:${order.user.id}:${order.items[0]}"

    // 可重复Evict，复杂表达式
    @RedisCacheEvict(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    @RedisCacheEvict(cacheKey = cacheKeyTemplate, expressions = ["key", "value2"])
    fun evictMulti(key: String, value: Any?, value2: Any?) {}
}
