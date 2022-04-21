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

    @RedisCacheable(cacheKey = "accountName:%s", paramsNames = ["accountId"])
    fun testCacheNameBoolean(accountId: String): Boolean {
        return false
    }

    @RedisCacheable(cacheKey = "accountName:%s", paramsNames = ["accountId"])
    fun testCacheNameBooleanOrNull(accountId: String): Boolean? {
        return null
    }

    @RedisCacheable(cacheKey = "accountName:%s", paramsNames = ["accountId"])
    fun testCacheNameByte(accountId: String): Byte {
        return 1
    }

    @RedisCacheable(cacheKey = "accountName:%s", paramsNames = ["accountId"])
    fun testCacheNameByteOrNull(accountId: String): Byte? {
        return null
    }

    @RedisCacheable(cacheKey = "accountName:%s", paramsNames = ["accountId"])
    fun testCacheNameShort(accountId: String): Short {
        return 1
    }

    @RedisCacheable(cacheKey = "accountName:%s", paramsNames = ["accountId"])
    fun testCacheNameShortOrNull(accountId: String): Short? {
        return null
    }

    @RedisCacheable(cacheKey = "accountName:%s", paramsNames = ["accountId"])
    fun testCacheNameInt(accountId: String): Int {
        return 123
    }

    @RedisCacheable(cacheKey = "accountName:%s", paramsNames = ["accountId"])
    fun testCacheNameInteger(accountId: String): Int? {
        return null
    }

    @RedisCacheable(cacheKey = "accountName:%s", paramsNames = ["accountId"])
    fun testCacheNameLong(accountId: String): Long {
        return 123L
    }

    @RedisCacheable(cacheKey = "accountName:%s", paramsNames = ["accountId"])
    fun testCacheNameLongOrNull(accountId: String): Long? {
        return null
    }

    @RedisCacheable(cacheKey = "accountName:%s", paramsNames = ["accountId"])
    fun testCacheNameFloat(accountId: String): Float {
        return 123.1F
    }

    @RedisCacheable(cacheKey = "accountName:%s", paramsNames = ["accountId"])
    fun testCacheNameFloatOrNull(accountId: String): Float? {
        return null
    }

    @RedisCacheable(cacheKey = "accountName:%s", paramsNames = ["accountId"])
    fun testCacheNameDouble(accountId: String): Double {
        return 123.12
    }

    @RedisCacheable(cacheKey = "accountName:%s", paramsNames = ["accountId"])
    fun testCacheNameDoubleOrNull(accountId: String): Double? {
        return null
    }

    @RedisCacheable(cacheKey = "accountName:%s", paramsNames = ["accountId"])
    fun testCacheNameBigDecimal(accountId: String): BigDecimal {
        return "34583485892983498.4569495987654".toBigDecimal()
    }

    @RedisCacheable(cacheKey = "accountName:%s", paramsNames = ["accountId"])
    fun testCacheNameBigInteger(accountId: String): BigInteger {
        return "34583485892983498456456345345".toBigInteger()
    }

    @RedisCacheable(cacheKey = "accountName:%s", paramsNames = ["accountId"])
    fun testCacheNameMap(accountId: String): Map<String, String> {
        return mapOf("a" to "123")
    }

    @RedisCacheable(cacheKey = "accountName:%s", paramsNames = ["accountId"])
    fun testCacheNameObj(accountId: String): Person {
        return Person("tony", 33)
    }

    @RedisCacheEvict(cacheKey = "test1")
    fun rTestCache() {
        println("yeah")
    }
}
