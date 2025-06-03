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

@file:JvmName("Redis")

package tony.redis

/**
 * Redis 相关.
 * @author tangli
 * @date 2023/09/28 19:56
 * @since 1.0.0
 */
import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.ListOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.data.redis.core.script.RedisScript
import tony.SpringContexts
import tony.exception.ApiException
import tony.redis.service.RedisService
import tony.utils.toNumber

@get:JvmSynthetic
internal val redisTemplate: RedisTemplate<String, Any> by SpringContexts.getBeanByLazy("redisTemplate")

/**
 * redis服务
 */
@get:JvmSynthetic
public val redisService: RedisService by SpringContexts.getBeanByLazy()

/**
 * 值运算
 */
@get:JvmSynthetic
internal val valueOp: ValueOperations<String, Any> by lazy(LazyThreadSafetyMode.PUBLICATION) {
    redisTemplate.opsForValue()
}

/**
 * 列表操作
 */
@get:JvmSynthetic
internal val listOp: ListOperations<String, Any> by lazy(LazyThreadSafetyMode.PUBLICATION) {
    redisTemplate.opsForList()
}

/**
 * 散列运算
 */
@get:JvmSynthetic
internal val hashOp: HashOperations<String, String, Any> by lazy(LazyThreadSafetyMode.PUBLICATION) {
    redisTemplate.opsForHash()
}

/**
 * 简单锁脚本引用
 */
@get:JvmSynthetic
internal val lockScript: RedisScript<Long> =
    RedisScript.of(ClassPathResource("META-INF/scripts/lockKey.lua"), Long::class.java)

/**
 * 批量删除脚本
 * @date redis 3.2+
 */
@get:JvmSynthetic
internal val deleteKeyByPatternScript: RedisScript<Long?> =
    RedisScript.of(ClassPathResource("META-INF/scripts/deleteByKeyPatterns.lua"), Long::class.java)

@JvmSynthetic
internal fun <R : Number> Any?.toNum(type: Class<in R>): R? =
    when (this) {
        is CharSequence -> this.toNumber(type)
        is Number -> this.toNumber(type)
        null -> null
        else -> throw ApiException("Not support ${this::class.java}")
    }
