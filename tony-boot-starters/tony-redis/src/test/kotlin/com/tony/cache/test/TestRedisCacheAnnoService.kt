/**
 * TestRedisCacheAnnoService
 *
 * @author tangli
 * @since 2022/4/19 17:28
 */
package com.tony.cache.test

import com.tony.cache.annotation.RedisCacheEvict
import com.tony.cache.annotation.RedisCacheable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.BigInteger

@Service
class TestRedisCacheAnnoService {

    @RedisCacheable(cacheKey = "test1")
    fun testCache1(): String {
        return "test1"
    }

    @RedisCacheable(cacheKey = "test2")
    fun testCache2(): String {
        return "test2"
    }

    @RedisCacheable(cacheKey = "accountName:%s", expressions = ["accountId"])
    fun testCacheNameBoolean(accountId: String): Boolean {
        return false
    }

    @RedisCacheable(cacheKey = "accountName:%s", expressions = ["accountId"])
    fun testCacheNameBooleanOrNull(accountId: String): Boolean? {
        return null
    }

    @RedisCacheable(cacheKey = "accountName:%s", expressions = ["accountId"])
    fun testCacheNameByte(accountId: String): Byte {
        return 1
    }

    @RedisCacheable(cacheKey = "accountName:%s", expressions = ["accountId"])
    fun testCacheNameByteOrNull(accountId: String): Byte? {
        return null
    }

    @RedisCacheable(cacheKey = "accountName:%s", expressions = ["accountId"])
    fun testCacheNameShort(accountId: String): Short {
        return 1
    }

    @RedisCacheable(cacheKey = "accountName:%s", expressions = ["accountId"])
    fun testCacheNameShortOrNull(accountId: String): Short? {
        return null
    }

    @RedisCacheable(cacheKey = "accountName:%s", expressions = ["accountId"])
    fun testCacheNameInt(accountId: String): Int {
        return 123
    }

    @RedisCacheable(cacheKey = "accountName:%s", expressions = ["accountId"])
    fun testCacheNameInteger(accountId: String): Int? {
        return null
    }

    @RedisCacheable(cacheKey = "accountName:%s", expressions = ["accountId"])
    fun testCacheNameLong(accountId: String): Long {
        return 123L
    }

    @RedisCacheable(cacheKey = "accountName:%s", expressions = ["accountId"])
    fun testCacheNameLongOrNull(accountId: String): Long? {
        return null
    }

    @RedisCacheable(cacheKey = "accountName:%s", expressions = ["accountId"])
    fun testCacheNameFloat(accountId: String): Float {
        return 123.1F
    }

    @RedisCacheable(cacheKey = "accountName:%s", expressions = ["accountId"])
    fun testCacheNameFloatOrNull(accountId: String): Float? {
        return null
    }

    @RedisCacheable(cacheKey = "accountName:%s", expressions = ["accountId"])
    fun testCacheNameDouble(accountId: String): Double {
        return 123.12
    }

    @RedisCacheable(cacheKey = "accountName:%s", expressions = ["accountId"])
    fun testCacheNameDoubleOrNull(accountId: String): Double? {
        return null
    }

    @RedisCacheable(cacheKey = "accountName:%s", expressions = ["accountId"])
    fun testCacheNameBigDecimal(accountId: String): BigDecimal {
        return "34583485892983498.4569495987654".toBigDecimal()
    }

    @RedisCacheable(cacheKey = "accountName:%s", expressions = ["accountId"])
    fun testCacheNameBigInteger(accountId: String): BigInteger {
        return "34583485892983498456456345345".toBigInteger()
    }

    @RedisCacheable(cacheKey = "accountName:%s", expressions = ["accountId"])
    fun testCacheNameMap(accountId: String): Map<String, String> {
        return mapOf("a" to "123")
    }

    @RedisCacheable(cacheKey = "accountName:%s", expressions = ["accountId"])
    fun testCacheNameObj(accountId: String): Person {
        return Person("tony", 33)
    }

    @RedisCacheEvict(cacheKey = "test1")
    @RedisCacheEvict(cacheKey = "test2")
    fun rTestCache() {
        println("yeah")
    }

    @RedisCacheable(cacheKey = "testobj:%s%s%s", expressions = ["obj.name","obj2.name","obj3.name"])
    fun testCacheAnnoObj(obj: TestCacheObj, obj2: TestCacheObj, obj3: TestCacheObj): TestCacheObj {
        return TestCacheObj(obj.name + " cached")
    }
}

data class TestCacheObj(val name: String)
