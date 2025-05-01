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

package tony.test.redis.jackson.service

import tony.annotation.redis.RedisCacheable
import tony.test.redis.RedisTestIntEnum
import tony.test.redis.RedisTestStringEnum
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.BigInteger

@Service
class JacksonRedisCacheAspectService {

    companion object {
        const val cacheKeyPrefix = "cache:test:jackson"
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
