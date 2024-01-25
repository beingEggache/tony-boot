package com.tony.test.redis.fury.service

import com.tony.annotation.redis.RedisCacheable
import com.tony.test.redis.RedisTestIntEnum
import com.tony.test.redis.RedisTestStringEnum
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.BigInteger

@Service
class FuryRedisCacheAspectService {

    companion object {
        const val cacheKeyPrefix = "cache:test:fury"
        const val cacheKeyTemplate = "$cacheKeyPrefix:%s:%s"
    }

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun testBoolean(key: String, value: Boolean?): Boolean? = value

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun testNumber(key: String, value: Byte?): Byte? = value

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun testNumber(key: String, value: Short?): Short? = value

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun testNumber(key: String, value: Int?): Int? = value

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun testNumber(key: String, value: Long?): Long? = value

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun testNumber(key: String, value: Float?): Float? = value

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun testNumber(key: String, value: Double?): Double? = value

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun testNumber(key: String, value: BigDecimal?): BigDecimal? = value

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun testNumber(key: String, value: BigInteger?): BigInteger? = value

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun testString(key: String, value: String?): String? = value

    @RedisCacheable(cacheKey = "$cacheKeyPrefix:%s", expressions = ["key"])
    fun testArray(key: String, value: Array<*>?): Array<*>? = value

    @RedisCacheable(cacheKey = "$cacheKeyPrefix:%s", expressions = ["key"])
    fun testList(key: String, value: List<*>?): List<*>? = value

    @RedisCacheable(cacheKey = "$cacheKeyPrefix:%s", expressions = ["key"])
    fun testMap(key: String, value: Map<*, *>?): Map<*, *>? = value

    @RedisCacheable(cacheKey = "$cacheKeyPrefix:%s", expressions = ["key"])
    fun testObj(key: String, value: Any?): Any? = value

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun testIntEnum(key: String, value: RedisTestIntEnum?): RedisTestIntEnum? = value

    @RedisCacheable(cacheKey = cacheKeyTemplate, expressions = ["key", "value"])
    fun testStringEnum(key: String, value: RedisTestStringEnum?): RedisTestStringEnum? = value

}
